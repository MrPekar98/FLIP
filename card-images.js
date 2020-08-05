import { Renderer } from "./rendering.js";

export const cardImages = {
    _1clubs: "https://upload.wikimedia.org/wikipedia/commons/e/eb/AC.svg",
    _2clubs: "https://upload.wikimedia.org/wikipedia/commons/6/69/2C.svg",
    _3clubs: "https://upload.wikimedia.org/wikipedia/commons/7/70/3C.svg",
    _4clubs: "https://upload.wikimedia.org/wikipedia/commons/2/25/4C.svg",
    _5clubs: "https://upload.wikimedia.org/wikipedia/commons/6/6d/5C.svg",
    _6clubs: "https://upload.wikimedia.org/wikipedia/commons/6/62/6C.svg",
    _7clubs: "https://upload.wikimedia.org/wikipedia/commons/6/68/7C.svg",
    _8clubs: "https://upload.wikimedia.org/wikipedia/commons/9/94/8C.svg",
    _9clubs: "https://upload.wikimedia.org/wikipedia/commons/6/63/9C.svg",
    _10clubs: "https://upload.wikimedia.org/wikipedia/commons/c/c7/10C.svg",
    _11clubs: "https://upload.wikimedia.org/wikipedia/commons/1/11/JC.svg",
    _12clubs: "https://upload.wikimedia.org/wikipedia/commons/9/9e/QC.svg",
    _13clubs: "https://upload.wikimedia.org/wikipedia/commons/e/e1/KC.svg",
    _1diamonds: "https://upload.wikimedia.org/wikipedia/commons/6/6d/AD.svg",
    _2diamonds: "https://upload.wikimedia.org/wikipedia/commons/f/fb/2D.svg",
    _3diamonds: "https://upload.wikimedia.org/wikipedia/commons/5/57/3D.svg",
    _4diamonds: "https://upload.wikimedia.org/wikipedia/commons/c/c7/4D.svg",
    _5diamonds: "https://upload.wikimedia.org/wikipedia/commons/d/d9/5D.svg",
    _6diamonds: "https://upload.wikimedia.org/wikipedia/commons/c/cf/6D.svg",
    _7diamonds: "https://upload.wikimedia.org/wikipedia/commons/5/5a/7D.svg",
    _8diamonds: "https://upload.wikimedia.org/wikipedia/commons/8/80/8D.svg",
    _9diamonds: "https://upload.wikimedia.org/wikipedia/commons/0/09/9D.svg",
    _10diamonds: "https://upload.wikimedia.org/wikipedia/commons/8/83/10D.svg",
    _11diamonds: "https://upload.wikimedia.org/wikipedia/commons/3/33/JD.svg",
    _12diamonds: "https://upload.wikimedia.org/wikipedia/commons/6/63/QD.svg",
    _13diamonds: "https://upload.wikimedia.org/wikipedia/commons/0/06/KD.svg",
    _1hearts: "https://upload.wikimedia.org/wikipedia/commons/8/87/AH.svg",
    _2hearts: "https://upload.wikimedia.org/wikipedia/commons/9/9d/2H.svg",
    _3hearts: "https://upload.wikimedia.org/wikipedia/commons/3/3a/3H.svg",
    _4hearts: "https://upload.wikimedia.org/wikipedia/commons/6/6f/4H.svg",
    _5hearts: "https://upload.wikimedia.org/wikipedia/commons/0/03/5H.svg",
    _6hearts: "https://upload.wikimedia.org/wikipedia/commons/a/a2/6H.svg",
    _7hearts: "https://upload.wikimedia.org/wikipedia/commons/e/e1/7H.svg",
    _8hearts: "https://upload.wikimedia.org/wikipedia/commons/3/34/8H.svg",
    _9hearts: "https://upload.wikimedia.org/wikipedia/commons/e/e0/9H.svg",
    _10hearts: "https://upload.wikimedia.org/wikipedia/commons/8/8a/10H.svg",
    _11hearts: "https://upload.wikimedia.org/wikipedia/commons/1/15/JH.svg",
    _12hearts: "https://upload.wikimedia.org/wikipedia/commons/d/d2/QH.svg",
    _13hearts: "https://upload.wikimedia.org/wikipedia/commons/e/e5/KH.svg",
    _1spades: "https://upload.wikimedia.org/wikipedia/commons/2/27/AS.svg",
    _2spades: "https://upload.wikimedia.org/wikipedia/commons/d/de/2S.svg",
    _3spades: "https://upload.wikimedia.org/wikipedia/commons/c/ce/3S.svg",
    _4spades: "https://upload.wikimedia.org/wikipedia/commons/c/cc/4S.svg",
    _5spades: "https://upload.wikimedia.org/wikipedia/commons/a/ac/5S.svg",
    _6spades: "https://upload.wikimedia.org/wikipedia/commons/1/1f/6S.svg",
    _7spades: "https://upload.wikimedia.org/wikipedia/commons/a/a1/7S.svg",
    _8spades: "https://upload.wikimedia.org/wikipedia/commons/3/36/8S.svg",
    _9spades: "https://upload.wikimedia.org/wikipedia/commons/3/31/9S.svg",
    _10spades: "https://upload.wikimedia.org/wikipedia/commons/1/16/10S.svg",
    _11spades: "https://upload.wikimedia.org/wikipedia/commons/d/d9/JS.svg",
    _12spades: "https://upload.wikimedia.org/wikipedia/commons/3/35/QS.svg",
    _13spades: "https://upload.wikimedia.org/wikipedia/commons/5/5c/KS.svg",
}

export class CardImageLoader {
    /**
     * 
     * @param {Renderer} renderer 
     */
    constructor(renderer) {
        this.renderer = renderer;
        this.load();
    }

    load() {
        let loadedCount = Object.keys(cardImages).length;

        let emptyImages = [];

        for (let i = 0; i < 52; i++) {
            emptyImages.push(new Image());
        }

        let keys = Object.keys(cardImages);

        for (let i = 0; i < keys.length; i++) {
            let image = emptyImages.pop();

            image.onload = () => {
                loadedCount--;

                if (loadedCount <= 0) {
                    this.renderer.loadComplete();
                }
            };

            image.src = cardImages[keys[i]];
            cardImages[keys[i]] = image;
        }
    }

    /**
     * 
     * @param {Card} card 
     */
    getCardImage(card) {
        let lookup = `_${card.rank.valueOf()}${card.suit.toString().toLowerCase()}`;

        return cardImages[lookup];
    }
}

export const cardBackImage = new Image();
cardBackImage.src = "https://upload.wikimedia.org/wikipedia/commons/3/30/Card_back_05a.svg";

export const backgroundImage = new Image();
backgroundImage.src = "http://erlectionede.dk/bg.jpg";