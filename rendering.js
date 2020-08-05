import { Pile } from "./pile.js";
import { CardImageLoader, cardBackImage, backgroundImage } from "./card-images.js";

class Bounds {
    constructor(left, top, right, bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}

class GraphicPile {
    /**
     *
     * @param {int} id
     * @param {Array} pile
     * @param {Bounds} bounds
     * @param {CardBounds[]} cardBounds
     */
    constructor(id, pile, bounds, cardBounds) {
        this.id = id;
        this.pile = pile;
        this.bounds = bounds;
        this.cardBounds = cardBounds || [];
    }
}

class CardBounds {
    /**
     * 
     * @param {Array} pile 
     * @param {Number} pileIndex 
     * @param {Bounds} bounds 
     */
    constructor(pile, pileIndex, bounds) {
        this.pile = pile;
        this.pileIndex = pileIndex;
        this.bounds = bounds;
    }
}

class Card {
    /**
     * 
     * @param {Suit} suit 
     * @param {Number} value 
     * @param {FaceDirection} direction 
     */
    constructor(suit, value, direction) {
        this.suit = suit;
        this.value = value;
        this.direction = direction;
    }
}

export class Renderer {
    /**
     * 
     * @param {Array<Pile>} deck
     * @param {Array<Pile>} piles 
     * @param {Array<Player>} players 
     */
    constructor(deck, piles, players) {
        this.deck = deck;
        this.cardImageLoader = new CardImageLoader(this);
        this.piles = piles;
        this.players = players;
        this.currentPlayer = 0;
        this.playerPiles = this.getPlayerPiles(this.players);
        this.gameStarted = false;

        this.bgCanvas = document.getElementById("bg-canvas");
        this.pileCanvas = document.getElementById("pile-canvas");
        this.uiCanvas = document.getElementById("ui-canvas");
        this.bgCanvas.height = window.innerHeight;
        this.bgCanvas.width = document.body.clientWidth;
        this.pileCanvas.height = window.innerHeight;
        this.pileCanvas.width = document.body.clientWidth;
        this.uiCanvas.height = window.innerHeight;
        this.uiCanvas.width = document.body.clientWidth;

        this.bgContext = this.bgCanvas.getContext("2d");
        this.bgContext.translate(0.5, 0.5);

        this.pileContext = this.pileCanvas.getContext("2d");
        this.pileContext.translate(0.5, 0.5);

        this.hoveringPile = false;
        this.uiContext = this.uiCanvas.getContext("2d");
        this.uiContext.translate(0.5, 0.5);

        this.cardRatio = 3.5 / 2.5;

        this.canvasWidth = this.bgCanvas.width;
        this.canvasHeight = this.bgCanvas.height;
        this.offsetX = this.bgCanvas.offsetLeft;
        this.offsetY = this.bgCanvas.offsetTop;

        this._rowCount = 2;
        this._columnCount = 10;

        this._pileWidth = this.canvasWidth / this._columnCount * 0.9;
        this._pileHeight = this.canvasHeight / this._rowCount * 0.9;
        this._cardWidth = this._pileWidth - 12;
        this._cardHeight = this._cardWidth * this.cardRatio;
        this._defaultYSpacing = this._cardHeight * (25 / 152);

        this.graphicPiles = [];

        this.offsetX = this.uiCanvas.offsetLeft;
        this.offsetY = this.uiCanvas.offsetTop;

        this.listenForChanges();
        document.onmousemove = e => {
            this.mouseHover(this.getMousePosition(e));
        }
    }

    listenForChanges() {
        _.observe(this.piles, function () {
            if (!this.gameStarted) {
                return;
            }

            this.drawPiles();
        }.bind(this));

        _.observe(this.players, function () {
            if (!this.gameStarted) {
                return;
            }

            this.drawPiles();
        }.bind(this));
    }

    setCurrentPlayer(currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.handsHidden = false;
        this.drawPiles();
    }

    loadComplete() {
        this.gameStarted = true;
        this.drawBackground();
        this.drawPiles();
    }

    hideHands() {
        this.handsHidden = true;
        this.drawPiles();
    }

    drawBackground() {
        this.clearCanvas(this.bgContext);

        this.bgContext.drawImage(backgroundImage, 0, 0, this.canvasWidth, this.canvasHeight);

        let x = (this.canvasWidth / this._columnCount - this._pileWidth) / 2 + 9 * this.canvasWidth / this._columnCount;
        let y = (this.canvasHeight / 2 - this._cardHeight / 2);

        let count = this.deck.cards.length;

        if (count) {
            this.bgContext.save();
            this.drawCard(this.bgContext, null, false, x + 6, y + 6, this._cardWidth, this._cardHeight);
            this.bgContext.font = "40px Arial";
            this.bgContext.fillStyle = "#FFFFFF";
            this.bgContext.strokeStyle = "#555555";
            let w = this.bgContext.measureText(String(count)).width;
            this.bgContext.strokeText(count, x + (10 + this._cardWidth - w) / 2, y + (this._cardHeight + 28) / 2);
            this.bgContext.fillText(count, x + (10 + this._cardWidth - w) / 2, y + (this._cardHeight + 28) / 2);
            this.bgContext.restore();
        }

        this.bgContext.save();

        this.bgContext.globalCompositeOperation = 'overlay';
        this.bgContext.strokeStyle = "#DDDDDD";
        this.bgContext.fillStyle = "#DDDDDD";
        this.bgContext.font = "18px Arial";
        this.bgContext.lineWidth = 0.04 * this._cardWidth;

        let playerText = this.players[this.currentPlayer].toString();
        let playerWidth = this.bgContext.measureText(playerText).width;
        this.bgContext.fillText(playerText, (this.canvasWidth - playerWidth) / 2, (this.canvasHeight - 8) / 2);
        this.bgContext.fillText(playerText, (this.canvasWidth - playerWidth) / 2, (this.canvasHeight - 8) / 2);

        this.bgContext.font = "14px Arial";

        let i = 0;

        for (let row = 0; row < this._rowCount; row++) {
            for (let column = 0; column < this._columnCount; column++) {
                let pile;
                if (row == 0) {
                    pile = this.piles[i++];
                } else {
                    pile = this.playerPiles[this.currentPlayer][i++];
                }

                if (!pile) {
                    i = 0;
                    break;
                }

                let x = (this.canvasWidth / this._columnCount - this._pileWidth) / 2 + column * this.canvasWidth / this._columnCount;
                let y = (this.canvasHeight / this._rowCount - this._pileHeight) / 2 + row * this.canvasHeight / this._rowCount;

                let text = pile.shortName;
                let textWidth = this.bgContext.measureText(text).width;
                this.bgContext.fillText(text, x + (this._pileWidth - textWidth) / 2, y - 5, this._pileWidth);
                this.bgContext.strokeRect(x, y, this._pileWidth, this._cardHeight + 12);
            }
        }

        let name = "Deck";
        let nameWidth = this.bgContext.measureText(name).width;
        this.bgContext.fillText(name, x + (this._pileWidth - nameWidth) / 2, y - 5, this._pileWidth);
        this.bgContext.strokeRect(x, y, this._pileWidth, this._cardHeight + 12);

        this.bgContext.restore();
    }

    drawPiles() {
        this.drawBackground();
        this.clearCanvas(this.pileContext);
        this.graphicPiles = [];

        if (this.piles.length > 20) {
            return;
        }

        this.bgContext.lineWidth = 6;
        this.bgContext.strokeStyle = "rgba(255, 255, 255, 0.7)";
        this.bgContext.fillStyle = "rgba(0, 0, 0, 0.5)";

        let i = 0;

        for (let row = 0; row < this._rowCount; row++) {
            for (let column = 0; column < this._columnCount; column++) {
                let pile;
                if (row == 0) {
                    // Draw piles.
                    pile = this.piles[i];
                } else if (row == 1) {
                    // Draw current player piles.
                    if (this.handsHidden !== true) {
                        pile = this.playerPiles[this.currentPlayer][i];
                    }
                }

                if (!pile) {
                    i = 0;
                    break;
                }

                let graphicPile = new GraphicPile(i, pile);

                let x = (this.canvasWidth / this._columnCount - this._pileWidth) / 2 + column * this.canvasWidth / this._columnCount;
                let y = (this.canvasHeight / this._rowCount - this._pileHeight) / 2 + row * this.canvasHeight / this._rowCount;

                let minX = x + 6;
                let minY = y + 6;

                let stackHeight = this._cardHeight;

                if (pile.cards.length > 0) {
                    let cardCount = pile.cards.length;

                    let ySpacing = this._defaultYSpacing;

                    if (ySpacing * (cardCount - 1) + this._cardHeight > this._pileHeight - 12) {
                        ySpacing = (this._pileHeight - 12 - this._cardHeight) / (cardCount - 1);
                    }

                    for (let a = 0; a < cardCount; a++) {
                        stackHeight = this._cardHeight + a * ySpacing;

                        this.drawCard(this.pileContext, pile.cards[a], pile.faceUp, minX, minY + a * ySpacing, this._cardWidth);

                        graphicPile.cardBounds.push(new CardBounds(pile, a, new Bounds(minX, minY + a * ySpacing, minX + this._cardWidth, minY + a * ySpacing + this._cardHeight)));
                    }
                }

                graphicPile.bounds = new Bounds(minX, minY, minX + this._pileWidth, minY + stackHeight);

                this.graphicPiles.push(graphicPile);

                i++;
            }
        }
    }

    clearCanvas(context) {
        context.clearRect(0, 0, this.canvasWidth, this.canvasHeight);
    }

    /**
     * 
     * @param {Array} pile 
     */
    pileHovered(coordinates, pile) {
        this.hoveringPile = true;

        this.clearCanvas(this.uiContext);
        this.drawPilePeek(coordinates, pile);
    }

    drawPilePeek(coordinates, pile) {
        let hoveredCard = this.getCardFormCoordinates(pile, coordinates);

        if (!hoveredCard) {
            return;
        }

        let x = coordinates.x;
        let y = coordinates.y;

        let left = x < this.canvasWidth / 2;
        let top = y < this.canvasHeight / 2;

        let border = "#444444";
        let bg = "#FFFFFF";

        if (!top) {
            y = y - 200;
            // Y coordinate stays the same (top left corner)
        } if (!left) {
            x = x - 142;
        }

        this.uiContext.strokeStyle = border;
        this.uiContext.lineWidth = 0.5;
        this.uiContext.fillStyle = bg;

        this.drawCard(this.uiContext, hoveredCard, true, x, y, 142);
        this.uiContext.strokeRect(x, y, 142, 200);
    }

    /**
     * 
     * @param {GraphicPile} clickedPile 
     * @param {{x: Number, y: Number}} coordinates 
     * @returns {Array<Bounds>}
     */
    getCardFormCoordinates(clickedPile, coordinates) {
        let x = coordinates.x;
        let y = coordinates.y;

        for (let i = clickedPile.cardBounds.length - 1; i >= 0; i--) {
            let cardBounds = clickedPile.cardBounds[i].bounds;

            // Should work since the first elements in the array are rendered on top.
            if (x >= cardBounds.left && x <= cardBounds.right && y >= cardBounds.top && y <= cardBounds.bottom) {
                // SINGLE or ONLY_TOP.
                return clickedPile.pile.cards[i];

            }
        }

        return null;
    }

    mouseHover(coordinates) {
        let hoveredPile = this.getPileFromCoordinates(coordinates);

        if (hoveredPile) {
            this.pileHovered(coordinates, hoveredPile);
        } else {
            if (this.hoveringPile) {
                this.clearCanvas(this.uiContext);
                this.hoveringPile = false;
            }
        }
    }

    /**
     * 
     * @param {{x: Number, y: Number}} coordinates 
     * @returns {GraphicPile}
     */
    getPileFromCoordinates(coordinates) {
        let x = coordinates.x;
        let y = coordinates.y;

        for (let i = 0; i < this.graphicPiles.length; i++) {
            let bounds = this.graphicPiles[i].bounds;
            if (x >= bounds.left && x <= bounds.right && y >= bounds.top && y <= bounds.bottom) {
                return this.graphicPiles[i];
            }
        }
    }

    getMousePosition(e) {
        return {
            x: e.clientX - this.offsetX,
            y: e.clientY - this.offsetY
        };
    }


    /**
    * 
    * @param {CanvasRenderingContext2D} context
    * @param {Card} card
    * @param {Boolean} faceUp
    * @param {Number} x 
    * @param {Number} y 
    * @param {Number} width 
    */
    drawCard(context, card, faceUp, x, y, width) {
        let height = width * this.cardRatio;

        context.save();

        // context.lineWidth = 2;
        // context.strokeStyle = "rgba(0,0,0,0.85)";
        // context.strokeRect(x, y, width, height);


        context.fillStyle = "rgba(0,0,0, 0.35)";
        context.fillRect(x, y - 1, width, height);

        context.fillStyle = "#FFFFFF";
        context.save();
        context.shadowColor = "rgba(0,0,0, 0.55)";
        context.shadowOffsetY = +2;
        context.shadowOffsetX = +2;
        context.shadowBlur = 3;
        context.fillRect(x, y, width, height);
        context.restore();


        if (!faceUp) {
            context.drawImage(cardBackImage, 3, 2, 201, 296, x + 5, y + 5, width - 10, height - 10);
        } else {
            context.drawImage(this.cardImageLoader.getCardImage(card), 4, 6, 229, 323, x + 1, y + 1, width - 2, height - 2);
        }

        context.restore();
    }

    /**
     * 
     * @param {Array<Player>} players 
     */
    getPlayerPiles(players) {
        return players.map(player => {
            let res = [];
            let keys = Object.keys(player);
            for (let i = 0; i < keys.length; i++) {
                if (player[keys[i]] instanceof Pile) {
                    res.push(player[keys[i]]);
                }
            }

            return res;
        });
    }
}