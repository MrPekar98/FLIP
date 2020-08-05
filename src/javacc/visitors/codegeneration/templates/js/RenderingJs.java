package javacc.visitors.codegeneration.templates.js;

public class RenderingJs implements JsModuleFile {
  @Override
  public String getExportedNames() {
    return "Renderer";
  }
  
  @Override
  public String getName() {
    return "rendering.js";
  }
  
  @Override
  public String getContent() {
    return "import { Pile } from \"./pile.js\";\n" +
            "import { CardImageLoader, cardBackImage, backgroundImage } from \"./card-images.js\";\n" +
            "\n" +
            "class Bounds {\n" +
            "    constructor(left, top, right, bottom) {\n" +
            "        this.left = left;\n" +
            "        this.top = top;\n" +
            "        this.right = right;\n" +
            "        this.bottom = bottom;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "class GraphicPile {\n" +
            "    /**\n" +
            "     *\n" +
            "     * @param {int} id\n" +
            "     * @param {Array} pile\n" +
            "     * @param {Bounds} bounds\n" +
            "     * @param {CardBounds[]} cardBounds\n" +
            "     */\n" +
            "    constructor(id, pile, bounds, cardBounds) {\n" +
            "        this.id = id;\n" +
            "        this.pile = pile;\n" +
            "        this.bounds = bounds;\n" +
            "        this.cardBounds = cardBounds || [];\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "class CardBounds {\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {Array} pile \n" +
            "     * @param {Number} pileIndex \n" +
            "     * @param {Bounds} bounds \n" +
            "     */\n" +
            "    constructor(pile, pileIndex, bounds) {\n" +
            "        this.pile = pile;\n" +
            "        this.pileIndex = pileIndex;\n" +
            "        this.bounds = bounds;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "class Card {\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {Suit} suit \n" +
            "     * @param {Number} value \n" +
            "     * @param {FaceDirection} direction \n" +
            "     */\n" +
            "    constructor(suit, value, direction) {\n" +
            "        this.suit = suit;\n" +
            "        this.value = value;\n" +
            "        this.direction = direction;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "export class Renderer {\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {Array<Pile>} deck\n" +
            "     * @param {Array<Pile>} piles \n" +
            "     * @param {Array<Player>} players \n" +
            "     */\n" +
            "    constructor(deck, piles, players) {\n" +
            "        this.deck = deck;\n" +
            "        this.cardImageLoader = new CardImageLoader(this);\n" +
            "        this.piles = piles;\n" +
            "        this.players = players;\n" +
            "        this.currentPlayer = 0;\n" +
            "        this.playerPiles = this.getPlayerPiles(this.players);\n" +
            "        this.gameStarted = false;\n" +
            "\n" +
            "        this.bgCanvas = document.getElementById(\"bg-canvas\");\n" +
            "        this.pileCanvas = document.getElementById(\"pile-canvas\");\n" +
            "        this.uiCanvas = document.getElementById(\"ui-canvas\");\n" +
            "        this.bgCanvas.height = window.innerHeight;\n" +
            "        this.bgCanvas.width = document.body.clientWidth;\n" +
            "        this.pileCanvas.height = window.innerHeight;\n" +
            "        this.pileCanvas.width = document.body.clientWidth;\n" +
            "        this.uiCanvas.height = window.innerHeight;\n" +
            "        this.uiCanvas.width = document.body.clientWidth;\n" +
            "\n" +
            "        this.bgContext = this.bgCanvas.getContext(\"2d\");\n" +
            "        this.bgContext.translate(0.5, 0.5);\n" +
            "\n" +
            "        this.pileContext = this.pileCanvas.getContext(\"2d\");\n" +
            "        this.pileContext.translate(0.5, 0.5);\n" +
            "\n" +
            "        this.hoveringPile = false;\n" +
            "        this.uiContext = this.uiCanvas.getContext(\"2d\");\n" +
            "        this.uiContext.translate(0.5, 0.5);\n" +
            "\n" +
            "        this.cardRatio = 3.5 / 2.5;\n" +
            "\n" +
            "        this.canvasWidth = this.bgCanvas.width;\n" +
            "        this.canvasHeight = this.bgCanvas.height;\n" +
            "        this.offsetX = this.bgCanvas.offsetLeft;\n" +
            "        this.offsetY = this.bgCanvas.offsetTop;\n" +
            "\n" +
            "        this._rowCount = 2;\n" +
            "        this._columnCount = 10;\n" +
            "\n" +
            "        this._pileWidth = this.canvasWidth / this._columnCount * 0.9;\n" +
            "        this._pileHeight = this.canvasHeight / this._rowCount * 0.9;\n" +
            "        this._cardWidth = this._pileWidth - 12;\n" +
            "        this._cardHeight = this._cardWidth * this.cardRatio;\n" +
            "        this._defaultYSpacing = this._cardHeight * (25 / 152);\n" +
            "\n" +
            "        this.graphicPiles = [];\n" +
            "\n" +
            "        this.offsetX = this.uiCanvas.offsetLeft;\n" +
            "        this.offsetY = this.uiCanvas.offsetTop;\n" +
            "\n" +
            "        this.listenForChanges();\n" +
            "        document.onmousemove = e => {\n" +
            "            this.mouseHover(this.getMousePosition(e));\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    listenForChanges() {\n" +
            "        _.observe(this.piles, function () {\n" +
            "            if (!this.gameStarted) {\n" +
            "                return;\n" +
            "            }\n" +
            "\n" +
            "            this.drawPiles();\n" +
            "        }.bind(this));\n" +
            "\n" +
            "        _.observe(this.players, function () {\n" +
            "            if (!this.gameStarted) {\n" +
            "                return;\n" +
            "            }\n" +
            "\n" +
            "            this.drawPiles();\n" +
            "        }.bind(this));\n" +
            "    }\n" +
            "\n" +
            "    setCurrentPlayer(currentPlayer) {\n" +
            "        this.currentPlayer = currentPlayer;\n" +
            "        this.handsHidden = false;\n" +
            "        this.drawPiles();\n" +
            "    }\n" +
            "\n" +
            "    loadComplete() {\n" +
            "        this.gameStarted = true;\n" +
            "        this.drawBackground();\n" +
            "        this.drawPiles();\n" +
            "    }\n" +
            "\n" +
            "    hideHands() {\n" +
            "        this.handsHidden = true;\n" +
            "        this.drawPiles();\n" +
            "    }\n" +
            "\n" +
            "    drawBackground() {\n" +
            "        this.clearCanvas(this.bgContext);\n" +
            "\n" +
            "        this.bgContext.drawImage(backgroundImage, 0, 0, this.canvasWidth, this.canvasHeight);\n" +
            "\n" +
            "        let x = (this.canvasWidth / this._columnCount - this._pileWidth) / 2 + 9 * this.canvasWidth / this._columnCount;\n" +
            "        let y = (this.canvasHeight / 2 - this._cardHeight / 2);\n" +
            "\n" +
            "        let count = this.deck.cards.length;\n" +
            "\n" +
            "        if (count) {\n" +
            "            this.bgContext.save();\n" +
            "            this.drawCard(this.bgContext, null, false, x + 6, y + 6, this._cardWidth, this._cardHeight);\n" +
            "            this.bgContext.font = \"40px Arial\";\n" +
            "            this.bgContext.fillStyle = \"#FFFFFF\";\n" +
            "            this.bgContext.strokeStyle = \"#555555\";\n" +
            "            let w = this.bgContext.measureText(String(count)).width;\n" +
            "            this.bgContext.strokeText(count, x + (10 + this._cardWidth - w) / 2, y + (this._cardHeight + 28) / 2);\n" +
            "            this.bgContext.fillText(count, x + (10 + this._cardWidth - w) / 2, y + (this._cardHeight + 28) / 2);\n" +
            "            this.bgContext.restore();\n" +
            "        }\n" +
            "\n" +
            "        this.bgContext.save();\n" +
            "\n" +
            "        this.bgContext.globalCompositeOperation = 'overlay';\n" +
            "        this.bgContext.strokeStyle = \"#DDDDDD\";\n" +
            "        this.bgContext.fillStyle = \"#DDDDDD\";\n" +
            "        this.bgContext.font = \"18px Arial\";\n" +
            "        this.bgContext.lineWidth = 0.04 * this._cardWidth;\n" +
            "\n" +
            "        let playerText = this.players[this.currentPlayer].toString();\n" +
            "        let playerWidth = this.bgContext.measureText(playerText).width;\n" +
            "        this.bgContext.fillText(playerText, (this.canvasWidth - playerWidth) / 2, (this.canvasHeight - 8) / 2);\n" +
            "        this.bgContext.fillText(playerText, (this.canvasWidth - playerWidth) / 2, (this.canvasHeight - 8) / 2);\n" +
            "\n" +
            "        this.bgContext.font = \"14px Arial\";\n" +
            "\n" +
            "        let i = 0;\n" +
            "\n" +
            "        for (let row = 0; row < this._rowCount; row++) {\n" +
            "            for (let column = 0; column < this._columnCount; column++) {\n" +
            "                let pile;\n" +
            "                if (row == 0) {\n" +
            "                    pile = this.piles[i++];\n" +
            "                } else {\n" +
            "                    pile = this.playerPiles[this.currentPlayer][i++];\n" +
            "                }\n" +
            "\n" +
            "                if (!pile) {\n" +
            "                    i = 0;\n" +
            "                    break;\n" +
            "                }\n" +
            "\n" +
            "                let x = (this.canvasWidth / this._columnCount - this._pileWidth) / 2 + column * this.canvasWidth / this._columnCount;\n" +
            "                let y = (this.canvasHeight / this._rowCount - this._pileHeight) / 2 + row * this.canvasHeight / this._rowCount;\n" +
            "\n" +
            "                let text = pile.shortName;\n" +
            "                let textWidth = this.bgContext.measureText(text).width;\n" +
            "                this.bgContext.fillText(text, x + (this._pileWidth - textWidth) / 2, y - 5, this._pileWidth);\n" +
            "                this.bgContext.strokeRect(x, y, this._pileWidth, this._cardHeight + 12);\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        let name = \"Deck\";\n" +
            "        let nameWidth = this.bgContext.measureText(name).width;\n" +
            "        this.bgContext.fillText(name, x + (this._pileWidth - nameWidth) / 2, y - 5, this._pileWidth);\n" +
            "        this.bgContext.strokeRect(x, y, this._pileWidth, this._cardHeight + 12);\n" +
            "\n" +
            "        this.bgContext.restore();\n" +
            "    }\n" +
            "\n" +
            "    drawPiles() {\n" +
            "        this.drawBackground();\n" +
            "        this.clearCanvas(this.pileContext);\n" +
            "        this.graphicPiles = [];\n" +
            "\n" +
            "        if (this.piles.length > 20) {\n" +
            "            return;\n" +
            "        }\n" +
            "\n" +
            "        this.bgContext.lineWidth = 6;\n" +
            "        this.bgContext.strokeStyle = \"rgba(255, 255, 255, 0.7)\";\n" +
            "        this.bgContext.fillStyle = \"rgba(0, 0, 0, 0.5)\";\n" +
            "\n" +
            "        let i = 0;\n" +
            "\n" +
            "        for (let row = 0; row < this._rowCount; row++) {\n" +
            "            for (let column = 0; column < this._columnCount; column++) {\n" +
            "                let pile;\n" +
            "                if (row == 0) {\n" +
            "                    // Draw piles.\n" +
            "                    pile = this.piles[i];\n" +
            "                } else if (row == 1) {\n" +
            "                    // Draw current player piles.\n" +
            "                    if (this.handsHidden !== true) {\n" +
            "                        pile = this.playerPiles[this.currentPlayer][i];\n" +
            "                    }\n" +
            "                }\n" +
            "\n" +
            "                if (!pile) {\n" +
            "                    i = 0;\n" +
            "                    break;\n" +
            "                }\n" +
            "\n" +
            "                let graphicPile = new GraphicPile(i, pile);\n" +
            "\n" +
            "                let x = (this.canvasWidth / this._columnCount - this._pileWidth) / 2 + column * this.canvasWidth / this._columnCount;\n" +
            "                let y = (this.canvasHeight / this._rowCount - this._pileHeight) / 2 + row * this.canvasHeight / this._rowCount;\n" +
            "\n" +
            "                let minX = x + 6;\n" +
            "                let minY = y + 6;\n" +
            "\n" +
            "                let stackHeight = this._cardHeight;\n" +
            "\n" +
            "                if (pile.cards.length > 0) {\n" +
            "                    let cardCount = pile.cards.length;\n" +
            "\n" +
            "                    let ySpacing = this._defaultYSpacing;\n" +
            "\n" +
            "                    if (ySpacing * (cardCount - 1) + this._cardHeight > this._pileHeight - 12) {\n" +
            "                        ySpacing = (this._pileHeight - 12 - this._cardHeight) / (cardCount - 1);\n" +
            "                    }\n" +
            "\n" +
            "                    for (let a = 0; a < cardCount; a++) {\n" +
            "                        stackHeight = this._cardHeight + a * ySpacing;\n" +
            "\n" +
            "                        this.drawCard(this.pileContext, pile.cards[a], pile.faceUp, minX, minY + a * ySpacing, this._cardWidth);\n" +
            "\n" +
            "                        graphicPile.cardBounds.push(new CardBounds(pile, a, new Bounds(minX, minY + a * ySpacing, minX + this._cardWidth, minY + a * ySpacing + this._cardHeight)));\n" +
            "                    }\n" +
            "                }\n" +
            "\n" +
            "                graphicPile.bounds = new Bounds(minX, minY, minX + this._pileWidth, minY + stackHeight);\n" +
            "\n" +
            "                this.graphicPiles.push(graphicPile);\n" +
            "\n" +
            "                i++;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    clearCanvas(context) {\n" +
            "        context.clearRect(0, 0, this.canvasWidth, this.canvasHeight);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {Array} pile \n" +
            "     */\n" +
            "    pileHovered(coordinates, pile) {\n" +
            "        this.hoveringPile = true;\n" +
            "\n" +
            "        this.clearCanvas(this.uiContext);\n" +
            "        this.drawPilePeek(coordinates, pile);\n" +
            "    }\n" +
            "\n" +
            "    drawPilePeek(coordinates, pile) {\n" +
            "        let hoveredCard = this.getCardFormCoordinates(pile, coordinates);\n" +
            "\n" +
            "        if (!hoveredCard) {\n" +
            "            return;\n" +
            "        }\n" +
            "\n" +
            "        let x = coordinates.x;\n" +
            "        let y = coordinates.y;\n" +
            "\n" +
            "        let left = x < this.canvasWidth / 2;\n" +
            "        let top = y < this.canvasHeight / 2;\n" +
            "\n" +
            "        let border = \"#444444\";\n" +
            "        let bg = \"#FFFFFF\";\n" +
            "\n" +
            "        if (!top) {\n" +
            "            y = y - 200;\n" +
            "            // Y coordinate stays the same (top left corner)\n" +
            "        } if (!left) {\n" +
            "            x = x - 142;\n" +
            "        }\n" +
            "\n" +
            "        this.uiContext.strokeStyle = border;\n" +
            "        this.uiContext.lineWidth = 0.5;\n" +
            "        this.uiContext.fillStyle = bg;\n" +
            "\n" +
            "        this.drawCard(this.uiContext, hoveredCard, true, x, y, 142);\n" +
            "        this.uiContext.strokeRect(x, y, 142, 200);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {GraphicPile} clickedPile \n" +
            "     * @param {{x: Number, y: Number}} coordinates \n" +
            "     * @returns {Array<Bounds>}\n" +
            "     */\n" +
            "    getCardFormCoordinates(clickedPile, coordinates) {\n" +
            "        let x = coordinates.x;\n" +
            "        let y = coordinates.y;\n" +
            "\n" +
            "        for (let i = clickedPile.cardBounds.length - 1; i >= 0; i--) {\n" +
            "            let cardBounds = clickedPile.cardBounds[i].bounds;\n" +
            "\n" +
            "            // Should work since the first elements in the array are rendered on top.\n" +
            "            if (x >= cardBounds.left && x <= cardBounds.right && y >= cardBounds.top && y <= cardBounds.bottom) {\n" +
            "                // SINGLE or ONLY_TOP.\n" +
            "                return clickedPile.pile.cards[i];\n" +
            "\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        return null;\n" +
            "    }\n" +
            "\n" +
            "    mouseHover(coordinates) {\n" +
            "        let hoveredPile = this.getPileFromCoordinates(coordinates);\n" +
            "\n" +
            "        if (hoveredPile) {\n" +
            "            this.pileHovered(coordinates, hoveredPile);\n" +
            "        } else {\n" +
            "            if (this.hoveringPile) {\n" +
            "                this.clearCanvas(this.uiContext);\n" +
            "                this.hoveringPile = false;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {{x: Number, y: Number}} coordinates \n" +
            "     * @returns {GraphicPile}\n" +
            "     */\n" +
            "    getPileFromCoordinates(coordinates) {\n" +
            "        let x = coordinates.x;\n" +
            "        let y = coordinates.y;\n" +
            "\n" +
            "        for (let i = 0; i < this.graphicPiles.length; i++) {\n" +
            "            let bounds = this.graphicPiles[i].bounds;\n" +
            "            if (x >= bounds.left && x <= bounds.right && y >= bounds.top && y <= bounds.bottom) {\n" +
            "                return this.graphicPiles[i];\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    getMousePosition(e) {\n" +
            "        return {\n" +
            "            x: e.clientX - this.offsetX,\n" +
            "            y: e.clientY - this.offsetY\n" +
            "        };\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    /**\n" +
            "    * \n" +
            "    * @param {CanvasRenderingContext2D} context\n" +
            "    * @param {Card} card\n" +
            "    * @param {Boolean} faceUp\n" +
            "    * @param {Number} x \n" +
            "    * @param {Number} y \n" +
            "    * @param {Number} width \n" +
            "    */\n" +
            "    drawCard(context, card, faceUp, x, y, width) {\n" +
            "        let height = width * this.cardRatio;\n" +
            "\n" +
            "        context.save();\n" +
            "\n" +
            "        // context.lineWidth = 2;\n" +
            "        // context.strokeStyle = \"rgba(0,0,0,0.85)\";\n" +
            "        // context.strokeRect(x, y, width, height);\n" +
            "\n" +
            "\n" +
            "        context.fillStyle = \"rgba(0,0,0, 0.35)\";\n" +
            "        context.fillRect(x, y - 1, width, height);\n" +
            "\n" +
            "        context.fillStyle = \"#FFFFFF\";\n" +
            "        context.save();\n" +
            "        context.shadowColor = \"rgba(0,0,0, 0.55)\";\n" +
            "        context.shadowOffsetY = +2;\n" +
            "        context.shadowOffsetX = +2;\n" +
            "        context.shadowBlur = 3;\n" +
            "        context.fillRect(x, y, width, height);\n" +
            "        context.restore();\n" +
            "\n" +
            "\n" +
            "        if (!faceUp) {\n" +
            "            context.drawImage(cardBackImage, 3, 2, 201, 296, x + 5, y + 5, width - 10, height - 10);\n" +
            "        } else {\n" +
            "            context.drawImage(this.cardImageLoader.getCardImage(card), 4, 6, 229, 323, x + 1, y + 1, width - 2, height - 2);\n" +
            "        }\n" +
            "\n" +
            "        context.restore();\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {Array<Player>} players \n" +
            "     */\n" +
            "    getPlayerPiles(players) {\n" +
            "        return players.map(player => {\n" +
            "            let res = [];\n" +
            "            let keys = Object.keys(player);\n" +
            "            for (let i = 0; i < keys.length; i++) {\n" +
            "                if (player[keys[i]] instanceof Pile) {\n" +
            "                    res.push(player[keys[i]]);\n" +
            "                }\n" +
            "            }\n" +
            "\n" +
            "            return res;\n" +
            "        });\n" +
            "    }\n" +
            "}";
  }
}
