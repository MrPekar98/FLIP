package javacc.visitors.codegeneration.templates.js;

public class CardImagesJs implements JsModuleFile {
  @Override
  public String getExportedNames() {
    return "CardImageLoader, cardImages, cardBackImage, backgroundImage";
  }
  
  @Override
  public String getName() {
    return "card-images.js";
  }
  
  @Override
  public String getContent() {
    return "import { Renderer } from \"./rendering.js\";\n" +
            "\n" +
            "export const cardImages = {\n" +
            "    _1clubs: \"https://upload.wikimedia.org/wikipedia/commons/e/eb/AC.svg\",\n" +
            "    _2clubs: \"https://upload.wikimedia.org/wikipedia/commons/6/69/2C.svg\",\n" +
            "    _3clubs: \"https://upload.wikimedia.org/wikipedia/commons/7/70/3C.svg\",\n" +
            "    _4clubs: \"https://upload.wikimedia.org/wikipedia/commons/2/25/4C.svg\",\n" +
            "    _5clubs: \"https://upload.wikimedia.org/wikipedia/commons/6/6d/5C.svg\",\n" +
            "    _6clubs: \"https://upload.wikimedia.org/wikipedia/commons/6/62/6C.svg\",\n" +
            "    _7clubs: \"https://upload.wikimedia.org/wikipedia/commons/6/68/7C.svg\",\n" +
            "    _8clubs: \"https://upload.wikimedia.org/wikipedia/commons/9/94/8C.svg\",\n" +
            "    _9clubs: \"https://upload.wikimedia.org/wikipedia/commons/6/63/9C.svg\",\n" +
            "    _10clubs: \"https://upload.wikimedia.org/wikipedia/commons/c/c7/10C.svg\",\n" +
            "    _11clubs: \"https://upload.wikimedia.org/wikipedia/commons/1/11/JC.svg\",\n" +
            "    _12clubs: \"https://upload.wikimedia.org/wikipedia/commons/9/9e/QC.svg\",\n" +
            "    _13clubs: \"https://upload.wikimedia.org/wikipedia/commons/e/e1/KC.svg\",\n" +
            "    _1diamonds: \"https://upload.wikimedia.org/wikipedia/commons/6/6d/AD.svg\",\n" +
            "    _2diamonds: \"https://upload.wikimedia.org/wikipedia/commons/f/fb/2D.svg\",\n" +
            "    _3diamonds: \"https://upload.wikimedia.org/wikipedia/commons/5/57/3D.svg\",\n" +
            "    _4diamonds: \"https://upload.wikimedia.org/wikipedia/commons/c/c7/4D.svg\",\n" +
            "    _5diamonds: \"https://upload.wikimedia.org/wikipedia/commons/d/d9/5D.svg\",\n" +
            "    _6diamonds: \"https://upload.wikimedia.org/wikipedia/commons/c/cf/6D.svg\",\n" +
            "    _7diamonds: \"https://upload.wikimedia.org/wikipedia/commons/5/5a/7D.svg\",\n" +
            "    _8diamonds: \"https://upload.wikimedia.org/wikipedia/commons/8/80/8D.svg\",\n" +
            "    _9diamonds: \"https://upload.wikimedia.org/wikipedia/commons/0/09/9D.svg\",\n" +
            "    _10diamonds: \"https://upload.wikimedia.org/wikipedia/commons/8/83/10D.svg\",\n" +
            "    _11diamonds: \"https://upload.wikimedia.org/wikipedia/commons/3/33/JD.svg\",\n" +
            "    _12diamonds: \"https://upload.wikimedia.org/wikipedia/commons/6/63/QD.svg\",\n" +
            "    _13diamonds: \"https://upload.wikimedia.org/wikipedia/commons/0/06/KD.svg\",\n" +
            "    _1hearts: \"https://upload.wikimedia.org/wikipedia/commons/8/87/AH.svg\",\n" +
            "    _2hearts: \"https://upload.wikimedia.org/wikipedia/commons/9/9d/2H.svg\",\n" +
            "    _3hearts: \"https://upload.wikimedia.org/wikipedia/commons/3/3a/3H.svg\",\n" +
            "    _4hearts: \"https://upload.wikimedia.org/wikipedia/commons/6/6f/4H.svg\",\n" +
            "    _5hearts: \"https://upload.wikimedia.org/wikipedia/commons/0/03/5H.svg\",\n" +
            "    _6hearts: \"https://upload.wikimedia.org/wikipedia/commons/a/a2/6H.svg\",\n" +
            "    _7hearts: \"https://upload.wikimedia.org/wikipedia/commons/e/e1/7H.svg\",\n" +
            "    _8hearts: \"https://upload.wikimedia.org/wikipedia/commons/3/34/8H.svg\",\n" +
            "    _9hearts: \"https://upload.wikimedia.org/wikipedia/commons/e/e0/9H.svg\",\n" +
            "    _10hearts: \"https://upload.wikimedia.org/wikipedia/commons/8/8a/10H.svg\",\n" +
            "    _11hearts: \"https://upload.wikimedia.org/wikipedia/commons/1/15/JH.svg\",\n" +
            "    _12hearts: \"https://upload.wikimedia.org/wikipedia/commons/d/d2/QH.svg\",\n" +
            "    _13hearts: \"https://upload.wikimedia.org/wikipedia/commons/e/e5/KH.svg\",\n" +
            "    _1spades: \"https://upload.wikimedia.org/wikipedia/commons/2/27/AS.svg\",\n" +
            "    _2spades: \"https://upload.wikimedia.org/wikipedia/commons/d/de/2S.svg\",\n" +
            "    _3spades: \"https://upload.wikimedia.org/wikipedia/commons/c/ce/3S.svg\",\n" +
            "    _4spades: \"https://upload.wikimedia.org/wikipedia/commons/c/cc/4S.svg\",\n" +
            "    _5spades: \"https://upload.wikimedia.org/wikipedia/commons/a/ac/5S.svg\",\n" +
            "    _6spades: \"https://upload.wikimedia.org/wikipedia/commons/1/1f/6S.svg\",\n" +
            "    _7spades: \"https://upload.wikimedia.org/wikipedia/commons/a/a1/7S.svg\",\n" +
            "    _8spades: \"https://upload.wikimedia.org/wikipedia/commons/3/36/8S.svg\",\n" +
            "    _9spades: \"https://upload.wikimedia.org/wikipedia/commons/3/31/9S.svg\",\n" +
            "    _10spades: \"https://upload.wikimedia.org/wikipedia/commons/1/16/10S.svg\",\n" +
            "    _11spades: \"https://upload.wikimedia.org/wikipedia/commons/d/d9/JS.svg\",\n" +
            "    _12spades: \"https://upload.wikimedia.org/wikipedia/commons/3/35/QS.svg\",\n" +
            "    _13spades: \"https://upload.wikimedia.org/wikipedia/commons/5/5c/KS.svg\",\n" +
            "}\n" +
            "\n" +
            "export class CardImageLoader {\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {Renderer} renderer \n" +
            "     */\n" +
            "    constructor(renderer) {\n" +
            "        this.renderer = renderer;\n" +
            "        this.load();\n" +
            "    }\n" +
            "\n" +
            "    load() {\n" +
            "        let loadedCount = Object.keys(cardImages).length;\n" +
            "\n" +
            "        let emptyImages = [];\n" +
            "\n" +
            "        for (let i = 0; i < 52; i++) {\n" +
            "            emptyImages.push(new Image());\n" +
            "        }\n" +
            "\n" +
            "        let keys = Object.keys(cardImages);\n" +
            "\n" +
            "        for (let i = 0; i < keys.length; i++) {\n" +
            "            let image = emptyImages.pop();\n" +
            "\n" +
            "            image.onload = () => {\n" +
            "                loadedCount--;\n" +
            "\n" +
            "                if (loadedCount <= 0) {\n" +
            "                    this.renderer.loadComplete();\n" +
            "                }\n" +
            "            };\n" +
            "\n" +
            "            image.src = cardImages[keys[i]];\n" +
            "            cardImages[keys[i]] = image;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * \n" +
            "     * @param {Card} card \n" +
            "     */\n" +
            "    getCardImage(card) {\n" +
            "        let lookup = `_${card.rank.valueOf()}${card.suit.toString().toLowerCase()}`;\n" +
            "\n" +
            "        return cardImages[lookup];\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "export const cardBackImage = new Image();\n" +
            "cardBackImage.src = \"https://upload.wikimedia.org/wikipedia/commons/3/30/Card_back_05a.svg\";\n" +
            "\n" +
            "export const backgroundImage = new Image();\n" +
            "backgroundImage.src = \"http://erlectionede.dk/bg.jpg\";";
  }
}
