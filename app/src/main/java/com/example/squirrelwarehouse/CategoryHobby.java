package com.example.squirrelwarehouse;

import java.util.HashMap;

public class CategoryHobby {

    String product;
    HashMap<String,Integer> map = new HashMap<>();

    CategoryHobby(String product) {
        this.product = product;

        setDataInit();  // 데이터 초기화. 1000개의 데이터 매핑하는 함수
    }

    private void setDataInit() {

        // 문화/예술 > 0
        map.put("overskirt",0);
        map.put("oxcart",0);
        map.put("palace",0);
        map.put("panpipe",0);
        map.put("pedestal",0);
        map.put("pick",0);
        map.put("pickelhaube",0);
        map.put("picket fence",0);
        map.put("planetarium",0);
        map.put("Polaroid camera",0);
        map.put("poncho",0);
        map.put("prayer rug",0);
        map.put("projector",0);
        map.put("radio",0);
        map.put("recreational vehicle",0);
        map.put("reflex camera",0);
        map.put("sarong",0);
        map.put("sax",0);
        map.put("screen",0);
        map.put("shoji",0);
        map.put("sombrero",0);
        map.put("stage",0);
        map.put("steel drum",0);
        map.put("stole",0);
        map.put("stone wall",0);
        map.put("suit",0);
        map.put("tape player",0);
        map.put("theater curtain",0);
        map.put("throne",0);
        map.put("tile roof",0);
        map.put("totem pole",0);
        map.put("tripod",0);
        map.put("trombone",0);
        map.put("velvet",0);
        map.put("vestment",0);
        map.put("violin",0);
        map.put("groom",0);

        map.put("cassette",0);
        map.put("cassette player",0);
        map.put("CD player",0);
        map.put("cello",0);
        map.put("cinema",0);
        map.put("cornet",0);
        map.put("drum",0);
        map.put("drumstick",0);
        map.put("electric guitar",0);
        map.put("feather boa",0);
        map.put("flute",0);
        map.put("French horn",0);
        map.put("gong",0);
        map.put("grand piano",0);
        map.put("harmonica",0);
        map.put("harp",0);
        map.put("iPod",0);
        map.put("jigsaw puzzle",0);
        map.put("loudspeaker",0);
        map.put("maraca",0);
        map.put("marimba",0);
        map.put("maypole",0);
        map.put("microphone",0);
        map.put("mitten",0);
        map.put("oboe",0);
        map.put("ocarina",0);
        map.put("organ",0);



        // 공예 > 1
        map.put("power drill",1);
        map.put("quilt",1);
        map.put("rule",1);
        map.put("screw",1);
        map.put("screwdriver",1);
        map.put("sewing machine",1);
        map.put("slide rule",1);
        map.put("thimble",1);
        map.put("wool",1);

        map.put("carpenter's kit",1);
        map.put("carton",1);
        map.put("chain saw",1);
        map.put("chiffonier",1);
        map.put("china cabinet",1);
        map.put("coil",1);
        map.put("cradle",1);
        map.put("crate",1);
        map.put("crib",1);
        map.put("dining table",1);
        map.put("disk brake",1);
        map.put("entertainment center",1);
        map.put("fire screen",1);
        map.put("four-poster",1);
        map.put("grille",1);
        map.put("hair slide",1);
        map.put("hammer",1);
        map.put("hatchet",1);
        map.put("hook",1);
        map.put("jack-o'-lantern",1);
        map.put("knot",1);
        map.put("lampshade",1);
        map.put("letter opener",1);
        map.put("lighter",1);
        map.put("loupe",1);
        map.put("lumbermill",1);
        map.put("mask",1);
        map.put("matchstick",1);
        map.put("mobile home",1);

        map.put("kite",1);



        // 요리 > 2
        map.put("bakery",2);

        map.put("pitcher",2);
        map.put("plate rack",2);
        map.put("pot",2);
        map.put("refrigerator",2);
        map.put("rotisserie",2);
        map.put("saltshaker",2);
        map.put("soup bowl",2);
        map.put("spatula",2);
        map.put("stove",2);
        map.put("strainer",2);
        map.put("teapot",2);
        map.put("toaster",2);
        map.put("tray",2);
        map.put("waffle iron",2);
        map.put("wine bottle",2);
        map.put("wok",2);
        map.put("wooden spoon",2);
        map.put("plate",2);
        map.put("guacamole",2);
        map.put("consomme",2);
        map.put("hot pot",2);
        map.put("trifle",2);
        map.put("ice cream",2);
        map.put("ice lolly",2);
        map.put("French loaf",2);
        map.put("bagel",2);
        map.put("pretzel",2);
        map.put("cheeseburger",2);
        map.put("hotdog",2);
        map.put("mashed potato",2);
        map.put("head cabbage",2);
        map.put("broccoli",2);
        map.put("cauliflower",2);
        map.put("zucchini",2);
        map.put("spaghetti squash",2);
        map.put("acorn squash",2);
        map.put("butternut squash",2);
        map.put("cucumber",2);
        map.put("artichoke",2);
        map.put("bell pepper",2);
        map.put("mushroom",2);
        map.put("Granny Smith",2);
        map.put("strawberry",2);
        map.put("orange",2);
        map.put("lemon",2);
        map.put("fig",2);
        map.put("pineapple",2);
        map.put("banana",2);
        map.put("jackfruit",2);
        map.put("custard apple",2);
        map.put("pomegranate",2);
        map.put("carbonara",2);
        map.put("chocolate sauce",2);
        map.put("dough",2);
        map.put("meat loaf",2);
        map.put("pizza",2);
        map.put("potpie",2);
        map.put("burrito",2);
        map.put("red wine",2);
        map.put("espresso",2);
        map.put("cup",2);
        map.put("eggnog",2);
        map.put("corn",2);
        map.put("acorn",2);

        map.put("butcher shop",2);
        map.put("caldron",2);
        map.put("can opener",2);
        map.put("cleaver",2);
        map.put("cocktail shaker",2);
        map.put("coffee mug",2);
        map.put("coffeepot",2);
        map.put("confectionery",2);
        map.put("corkscrew",2);
        map.put("Crock Pot",2);
        map.put("dishrag",2);
        map.put("Dutch oven",2);
        map.put("frying pan",2);
        map.put("goblet",2);
        map.put("grocery store",2);
        map.put("hamper",2);
        map.put("ladle",2);
        map.put("measuring cup",2);
        map.put("microwave",2);
        map.put("milk can",2);
        map.put("mixing bowl",2);



        // 미술 > 3
        map.put("balloon",3);

        map.put("paintbrush",3);

        map.put("nail",3);



        // 운동/스포츠 > 4
        map.put("balance beam",4);
        map.put("barbell",4);
        map.put("baseball",4);
        map.put("basketball",4);
        map.put("bicycle-built-for-two",4);
        map.put("binder",4);
        map.put("bobsled",4);

        map.put("paddle",4);
        map.put("parachute",4);
        map.put("parallel bars",4);
        map.put("ping-pong ball",4);
        map.put("pool table",4);
        map.put("puck",4);
        map.put("punching bag",4);
        map.put("racer",4);
        map.put("racket",4);
        map.put("rugby ball",4);
        map.put("running shoe",4);
        map.put("schooner",4);
        map.put("scoreboard",4);
        map.put("ski",4);
        map.put("ski mask",4);
        map.put("sleeping bag",4);
        map.put("snorkel",4);
        map.put("snowmobile",4);
        map.put("soccer ball",4);
        map.put("speedboat",4);
        map.put("sports car",4);
        map.put("stopwatch",4);
        map.put("swimming trunks",4);
        map.put("swing",4);
        map.put("tennis ball",4);
        map.put("tricycle",4);
        map.put("trimaran",4);
        map.put("unicycle",4);
        map.put("volleyball",4);
        map.put("whistle",4);
        map.put("yawl",4);
        map.put("ballplayer",4);
        map.put("scuba diver",4);

        map.put("breastplate",4);
        map.put("buckle",4);
        map.put("bulletproof vest",4);
        map.put("cannon",4);
        map.put("canoe",4);
        map.put("car wheel",4);
        map.put("catamaran",4);
        map.put("cliff dwelling",4);
        map.put("convertible",4);
        map.put("crash helmet",4);
        map.put("croquet ball",4);
        map.put("cuirass",4);
        map.put("dogsled",4);
        map.put("dumbbell",4);
        map.put("football helmet",4);
        map.put("gasmask",4);
        map.put("go-kart",4);
        map.put("golf ball",4);
        map.put("golfcart",4);
        map.put("gondola",4);
        map.put("holster",4);
        map.put("horizontal bar",4);
        map.put("jersey",4);
        map.put("knee pad",4);
        map.put("magnetic compass",4);
        map.put("maillot",4);
        map.put("military uniform",4);
        map.put("mortar",4);
        map.put("mountain bike",4);
        map.put("mountain tent",4);
        map.put("neck brace",4);
        map.put("odometer",4);
        map.put("oil filter",4);




        // 원예 > 5
        map.put("plow",5);
        map.put("shovel",5);
        map.put("vase",5);
        map.put("cardoon",5);
        map.put("rapeseed",5);
        map.put("daisy",5);
        map.put("yellow lady's slipper",5);

        map.put("chainlink fence",5);
        map.put("fountain",5);
        map.put("greenhouse",5);
        map.put("hand blower",5);
        map.put("honeycomb",5);
        map.put("lawn mower",5);



        // 공부 > 6
        map.put("abacus",6);
        map.put("barometer",6);

        map.put("pencil box",6);
        map.put("pencil sharpener",6);
        map.put("rubber eraser",6);
        map.put("book jacket",6);

        map.put("desk",6);
        map.put("file",6);
        map.put("folding chair",6);
        map.put("fountain pen",6);
        map.put("mortarboard",6);



        // 게임 > 7
        map.put("slot",7);

        map.put("desktop computer",7);
        map.put("hard disc",7);
        map.put("joystick",7);
        map.put("laptop",7);
        map.put("maze",7);
        map.put("modem",7);
        map.put("monitor",7);
        map.put("mouse",7);
        map.put("notebook",7);



        // 기타 > 8
        map.put("Old English sheepdog",8);
        map.put("Shetland sheepdog",8);
        map.put("collie",8);
        map.put("Border collie",8);
        map.put("Bouvier des Flandres",8);
        map.put("Rottweiler",8);
        map.put("German shepherd",8);
        map.put("Doberman",8);
        map.put("miniature pinscher",8);
        map.put("Greater Swiss Mountain dog",8);
        map.put("Bernese mountain dog",8);
        map.put("Appenzeller",8);
        map.put("EntleBucher",8);
        map.put("boxer",8);
        map.put("bull mastiff",8);
        map.put("Tibetan mastiff",8);
        map.put("French bulldog",8);
        map.put("Great Dane",8);
        map.put("Saint Bernard",8);
        map.put("Eskimo dog",8);
        map.put("malamute",8);
        map.put("Siberian husky",8);
        map.put("dalmatian",8);
        map.put("affenpinscher",8);
        map.put("basenji",8);
        map.put("pug",8);
        map.put("Leonberg",8);
        map.put("Newfoundland",8);
        map.put("Great Pyrenees",8);
        map.put("Samoyed",8);
        map.put("Pomeranian",8);
        map.put("chow",8);
        map.put("keeshond",8);
        map.put("Brabancon griffon",8);
        map.put("Pembroke",8);
        map.put("Cardigan",8);
        map.put("toy poodle",8);
        map.put("miniature poodle",8);
        map.put("standard poodle",8);
        map.put("Mexican hairless",8);
        map.put("timber wolf",8);
        map.put("white wolf",8);
        map.put("red wolf",8);
        map.put("coyote",8);
        map.put("dingo",8);
        map.put("dhole",8);
        map.put("African hunting dog",8);
        map.put("hyena",8);
        map.put("red fox",8);
        map.put("kit fox",8);
        map.put("Arctic fox",8);
        map.put("grey fox",8);
        map.put("tabby",8);
        map.put("tiger cat",8);
        map.put("Persian cat",8);
        map.put("Siamese cat",8);
        map.put("Egyptian cat",8);
        map.put("cougar",8);
        map.put("lynx",8);
        map.put("leopard",8);
        map.put("snow leopard",8);
        map.put("jaguar",8);
        map.put("lion",8);
        map.put("tiger",8);
        map.put("cheetah",8);
        map.put("brown bear",8);
        map.put("American black bear",8);
        map.put("ice bear",8);
        map.put("sloth bear",8);
        map.put("mongoose",8);
        map.put("meerkat",8);
        map.put("tiger beetle",8);
        map.put("ladybug",8);
        map.put("ground beetle",8);
        map.put("long-horned beetle",8);
        map.put("leaf beetle",8);
        map.put("dung beetle",8);
        map.put("rhinoceros beetle",8);
        map.put("weevil",8);
        map.put("fly",8);
        map.put("bee",8);
        map.put("ant",8);
        map.put("grasshopper",8);
        map.put("cricket",8);
        map.put("walking stick",8);
        map.put("cockroach",8);
        map.put("mantis",8);
        map.put("cicada",8);
        map.put("leafhopper",8);
        map.put("lacewing",8);
        map.put("dragonfly",8);
        map.put("damselfly",8);
        map.put("admiral",8);
        map.put("ringlet",8);
        map.put("monarch",8);
        map.put("cabbage butterfly",8);
        map.put("sulphur butterfly",8);
        map.put("lycaenid",8);
        map.put("starfish",8);
        map.put("sea urchin",8);
        map.put("sea cucumber",8);
        map.put("wood rabbit",8);
        map.put("hare",8);
        map.put("Angora",8);
        map.put("hamster",8);
        map.put("porcupine",8);
        map.put("fox squirrel",8);
        map.put("marmot",8);
        map.put("beaver",8);
        map.put("guinea pig",8);
        map.put("sorrel",8);
        map.put("zebra",8);
        map.put("hog",8);
        map.put("wild boar",8);
        map.put("warthog",8);
        map.put("hippopotamus",8);
        map.put("ox",8);
        map.put("water buffalo",8);
        map.put("bison",8);
        map.put("ram",8);
        map.put("bighorn",8);
        map.put("ibex",8);
        map.put("hartebeest",8);
        map.put("impala",8);
        map.put("gazelle",8);
        map.put("Arabian camel",8);
        map.put("llama",8);
        map.put("weasel",8);
        map.put("mink",8);
        map.put("polecat",8);
        map.put("black-footed ferret",8);
        map.put("otter",8);
        map.put("skunk",8);
        map.put("badger",8);
        map.put("armadillo",8);
        map.put("three-toed sloth",8);
        map.put("orangutan",8);
        map.put("gorilla",8);
        map.put("chimpanzee",8);
        map.put("gibbon",8);
        map.put("siamang",8);
        map.put("guenon",8);
        map.put("patas",8);
        map.put("baboon",8);
        map.put("macaque",8);
        map.put("langur",8);
        map.put("colobus",8);
        map.put("proboscis monkey",8);
        map.put("marmoset",8);
        map.put("capuchin",8);
        map.put("howler monkey",8);
        map.put("titi",8);
        map.put("spider monkey",8);
        map.put("squirrel monkey",8);
        map.put("Madagascar cat",8);
        map.put("indri",8);
        map.put("Indian elephant",8);
        map.put("African elephant",8);
        map.put("lesser panda",8);
        map.put("giant panda",8);
        map.put("barracouta",8);
        map.put("eel",8);
        map.put("coho",8);
        map.put("rock beauty",8);
        map.put("anemone fish",8);
        map.put("sturgeon",8);
        map.put("gar",8);
        map.put("lionfish",8);
        map.put("puffer",8);
        map.put("abaya",8);
        map.put("academic gown",8);
        map.put("accordion",8);
        map.put("acoustic guitar",8);
        map.put("aircraft carrier",8);
        map.put("airliner",8);
        map.put("airship",8);
        map.put("altar",8);
        map.put("ambulance",8);
        map.put("amphibian",8);
        map.put("analog clock",8);
        map.put("apiary",8);
        map.put("apron",8);
        map.put("ashcan",8);
        map.put("assault rifle",8);
        map.put("backpack",8);
        map.put("ballpoint",8);
        map.put("Band Aid",8);
        map.put("banjo",8);
        map.put("bannister",8);
        map.put("barber chair",8);
        map.put("barbershop",8);
        map.put("barn",8);
        map.put("barrel",8);
        map.put("barrow",8);
        map.put("bassinet",8);
        map.put("bassoon",8);
        map.put("bathing cap",8);
        map.put("bath towel",8);
        map.put("bathtub",8);
        map.put("beach wagon",8);
        map.put("beacon",8);
        map.put("bearskin",8);
        map.put("beer bottle",8);
        map.put("beer glass",8);
        map.put("bell cote",8);
        map.put("bikini",8);
        map.put("binoculars",8);
        map.put("birdhouse",8);
        map.put("boathouse",8);
        map.put("bolo tie",8);
        map.put("bonnet",8);
        map.put("bookcase",8);
        map.put("bookshop",8);
        map.put("bottlecap",8);
        map.put("bow",8);
        map.put("bow tie",8);
        map.put("brass",8);

        map.put("oxygen mask",8);
        map.put("packet",8);
        map.put("paddlewheel",8);
        map.put("padlock",8);
        map.put("pajama",8);
        map.put("paper towel",8);
        map.put("park bench",8);
        map.put("parking meter",8);
        map.put("passenger car",8);
        map.put("patio",8);
        map.put("pay-phone",8);
        map.put("perfume",8);
        map.put("petri dish",8);
        map.put("photocopier",8);
        map.put("pickup",8);
        map.put("pier",8);
        map.put("piggy bank",8);
        map.put("pill bottle",8);
        map.put("pillow",8);
        map.put("pinwheel",8);
        map.put("pirate",8);
        map.put("plane",8);
        map.put("plastic bag",8);
        map.put("plunger",8);
        map.put("pole",8);
        map.put("police van",8);
        map.put("pop bottle",8);
        map.put("printer",8);
        map.put("prison",8);
        map.put("projectile",8);
        map.put("purse",8);
        map.put("quill",8);
        map.put("radiator",8);
        map.put("radio telescope",8);
        map.put("rain barrel",8);
        map.put("reel",8);
        map.put("remote control",8);
        map.put("restaurant",8);
        map.put("revolver",8);
        map.put("rifle",8);
        map.put("rocking chair",8);
        map.put("safe",8);
        map.put("safety pin",8);
        map.put("sandal",8);
        map.put("scabbard",8);
        map.put("scale",8);
        map.put("school bus",8);
        map.put("seat belt",8);
        map.put("shield",8);
        map.put("shoe shop",8);
        map.put("shopping basket",8);
        map.put("shopping cart",8);
        map.put("shower cap",8);
        map.put("shower curtain",8);
        map.put("sliding door",8);
        map.put("snowplow",8);
        map.put("soap dispenser",8);
        map.put("sock",8);
        map.put("solar dish",8);
        map.put("space bar",8);
        map.put("space heater",8);
        map.put("space shuttle",8);
        map.put("spider web",8);
        map.put("spindle",8);
        map.put("spotlight",8);
        map.put("steam locomotive",8);
        map.put("steel arch bridge",8);
        map.put("stethoscope",8);
        map.put("streetcar",8);
        map.put("stretcher",8);
        map.put("studio couch",8);
        map.put("stupa",8);
        map.put("submarine",8);
        map.put("sundial",8);
        map.put("sunglass",8);
        map.put("sunglasses",8);
        map.put("sunscreen",8);
        map.put("suspension bridge",8);
        map.put("swab",8);
        map.put("sweatshirt",8);
        map.put("switch",8);
        map.put("table lamp",8);
        map.put("tank",8);
        map.put("teddy",8);
        map.put("television",8);
        map.put("thatch",8);
        map.put("thresher",8);
        map.put("tobacco shop",8);
        map.put("toilet seat",8);
        map.put("torch",8);
        map.put("tow truck",8);
        map.put("toyshop",8);
        map.put("tractor",8);
        map.put("trailer truck",8);
        map.put("trench coat",8);
        map.put("triumphal arch",8);
        map.put("trolleybus",8);
        map.put("tub",8);
        map.put("turnstile",8);
        map.put("typewriter keyboard",8);
        map.put("umbrella",8);
        map.put("upright",8);
        map.put("vacuum",8);
        map.put("vault",8);
        map.put("vending machine",8);
        map.put("viaduct",8);
        map.put("wall clock",8);
        map.put("wallet",8);
        map.put("wardrobe",8);
        map.put("warplane",8);
        map.put("washbasin",8);
        map.put("washer",8);
        map.put("water bottle",8);
        map.put("water jug",8);
        map.put("water tower",8);
        map.put("whiskey jug",8);
        map.put("wig",8);
        map.put("window screen",8);
        map.put("window shade",8);
        map.put("Windsor tie",8);
        map.put("wing",8);
        map.put("worm fence",8);
        map.put("wreck",8);
        map.put("yurt",8);
        map.put("web site",8);
        map.put("comic book",8);
        map.put("crossword puzzle",8);
        map.put("street sign",8);
        map.put("traffic light",8);
        map.put("menu",8);
        map.put("hay",8);
        map.put("alp",8);
        map.put("bubble",8);
        map.put("cliff",8);
        map.put("coral reef",8);
        map.put("geyser",8);
        map.put("lakeside",8);
        map.put("promontory",8);
        map.put("sandbar",8);
        map.put("seashore",8);
        map.put("valley",8);
        map.put("volcano",8);
        map.put("hip",8);
        map.put("buckeye",8);
        map.put("coral fungus",8);
        map.put("agaric",8);
        map.put("gyromitra",8);
        map.put("stinkhorn",8);
        map.put("earthstar",8);
        map.put("hen-of-the-woods",8);
        map.put("bolete",8);
        map.put("ear",8);
        map.put("toilet tissue",8);

        map.put("brassiere",8);
        map.put("breakwater",8);
        map.put("broom",8);
        map.put("bucket",8);
        map.put("bullet train",8);
        map.put("cab",8);
        map.put("candle",8);
        map.put("cardigan",8);
        map.put("car mirror",8);
        map.put("carousel",8);
        map.put("cash machine",8);
        map.put("castle",8);
        map.put("cellular telephone",8);
        map.put("chain",8);
        map.put("chain mail",8);
        map.put("chest",8);
        map.put("chime",8);
        map.put("Christmas stocking",8);
        map.put("church",8);
        map.put("cloak",8);
        map.put("clog",8);
        map.put("combination lock",8);
        map.put("computer keyboard",8);
        map.put("container ship",8);
        map.put("cowboy boot",8);
        map.put("cowboy hat",8);
        map.put("crane",8);
        map.put("crutch",8);
        map.put("dam",8);
        map.put("dial telephone",8);
        map.put("diaper",8);
        map.put("digital clock",8);
        map.put("digital watch",8);
        map.put("dishwasher",8);
        map.put("dock",8);
        map.put("dome",8);
        map.put("doormat",8);
        map.put("drilling platform",8);
        map.put("electric fan",8);
        map.put("electric locomotive",8);
        map.put("envelope",8);
        map.put("espresso maker",8);
        map.put("face powder",8);
        map.put("fireboat",8);
        map.put("fire engine",8);
        map.put("flagpole",8);
        map.put("forklift",8);
        map.put("freight car",8);
        map.put("fur coat",8);
        map.put("garbage truck",8);
        map.put("gas pump",8);
        map.put("gown",8);
        map.put("guillotine",8);
        map.put("hair spray",8);
        map.put("half track",8);
        map.put("hand-held computer",8);
        map.put("handkerchief",8);
        map.put("home theater",8);
        map.put("hoopskirt",8);
        map.put("horse cart",8);
        map.put("hourglass",8);
        map.put("iron",8);
        map.put("jean",8);
        map.put("jeep",8);
        map.put("jinrikisha",8);
        map.put("kimono",8);
        map.put("lab coat",8);
        map.put("lens cap",8);
        map.put("library",8);
        map.put("lifeboat",8);
        map.put("limousine",8);
        map.put("liner",8);
        map.put("lipstick",8);
        map.put("Loafer",8);
        map.put("lotion",8);
        map.put("mailbag",8);
        map.put("mailbox",8);
        map.put("manhole cover",8);
        map.put("medicine chest",8);
        map.put("megalith",8);
        map.put("minibus",8);
        map.put("miniskirt",8);
        map.put("minivan",8);
        map.put("missile",8);
        map.put("Model T",8);
        map.put("monastery",8);
        map.put("moped",8);
        map.put("mosque",8);
        map.put("mosquito net",8);
        map.put("motor scooter",8);
        map.put("mousetrap",8);
        map.put("moving van",8);
        map.put("muzzle",8);
        map.put("necklace",8);
        map.put("nipple",8);
        map.put("obelisk",8);
        map.put("oscilloscope",8);

        map.put("background",8);
        map.put("tench",8);
        map.put("goldfish",8);
        map.put("great white shark",8);
        map.put("tiger shark",8);
        map.put("hammerhead",8);
        map.put("electric ray",8);
        map.put("stingray",8);
        map.put("cock",8);
        map.put("hen",8);
        map.put("ostrich",8);
        map.put("brambling",8);
        map.put("goldfinch",8);
        map.put("house finch",8);
        map.put("junco",8);
        map.put("indigo bunting",8);
        map.put("robin",8);
        map.put("bulbul",8);
        map.put("jay",8);
        map.put("magpie",8);
        map.put("chickadee",8);
        map.put("water ouzel",8);
        map.put("bald eagle",8);
        map.put("vulture",8);
        map.put("great grey owl",8);
        map.put("European fire salamander",8);
        map.put("common newt",8);
        map.put("eft",8);
        map.put("spotted salamander",8);
        map.put("axolotl",8);
        map.put("bullfrog",8);
        map.put("tree frog",8);
        map.put("tailed frog",8);
        map.put("loggerhead",8);
        map.put("leatherback turtle",8);
        map.put("mud turtle",8);
        map.put("terrapin",8);
        map.put("box turtle",8);
        map.put("banded gecko",8);
        map.put("common iguana",8);
        map.put("American chameleon",8);
        map.put("whiptail",8);
        map.put("agama",8);
        map.put("frilled lizard",8);
        map.put("alligator lizard",8);
        map.put("Gila monster",8);
        map.put("green lizard",8);
        map.put("African chameleon",8);
        map.put("Komodo dragon",8);
        map.put("African crocodile",8);
        map.put("American alligator",8);
        map.put("triceratops",8);
        map.put("thunder snake",8);
        map.put("ringneck snake",8);
        map.put("hognose snake",8);
        map.put("green snake",8);
        map.put("king snake",8);
        map.put("garter snake",8);
        map.put("water snake",8);
        map.put("vine snake",8);
        map.put("night snake",8);
        map.put("boa constrictor",8);
        map.put("rock python",8);
        map.put("Indian cobra",8);
        map.put("green mamba",8);
        map.put("sea snake",8);
        map.put("horned viper",8);
        map.put("diamondback",8);
        map.put("sidewinder",8);
        map.put("trilobite",8);
        map.put("harvestman",8);
        map.put("scorpion",8);
        map.put("black and gold garden spider",8);
        map.put("barn spider",8);
        map.put("garden spider",8);
        map.put("black widow",8);
        map.put("tarantula",8);
        map.put("wolf spider",8);
        map.put("tick",8);
        map.put("centipede",8);
        map.put("black grouse",8);
        map.put("ptarmigan",8);
        map.put("ruffed grouse",8);
        map.put("prairie chicken",8);
        map.put("peacock",8);
        map.put("quail",8);
        map.put("partridge",8);
        map.put("African grey",8);
        map.put("macaw",8);
        map.put("sulphur-crested cockatoo",8);
        map.put("lorikeet",8);
        map.put("coucal",8);
        map.put("bee eater",8);
        map.put("hornbill",8);
        map.put("hummingbird",8);
        map.put("jacamar",8);
        map.put("toucan",8);
        map.put("drake",8);
        map.put("red-breasted merganser",8);
        map.put("goose",8);
        map.put("black swan",8);
        map.put("tusker",8);
        map.put("echidna",8);
        map.put("platypus",8);
        map.put("wallaby",8);
        map.put("koala",8);
        map.put("wombat",8);
        map.put("jellyfish",8);
        map.put("sea anemone",8);
        map.put("brain coral",8);
        map.put("flatworm",8);
        map.put("nematode",8);
        map.put("conch",8);
        map.put("snail",8);
        map.put("slug",8);
        map.put("sea slug",8);
        map.put("chiton",8);
        map.put("chambered nautilus",8);
        map.put("Dungeness crab",8);
        map.put("rock crab",8);
        map.put("fiddler crab",8);
        map.put("king crab",8);
        map.put("American lobster",8);
        map.put("spiny lobster",8);
        map.put("crayfish",8);
        map.put("hermit crab",8);
        map.put("isopod",8);
        map.put("white stork",8);
        map.put("black stork",8);
        map.put("spoonbill",8);
        map.put("flamingo",8);
        map.put("little blue heron",8);
        map.put("American egret",8);
        map.put("bittern",8);
        map.put("cairn",8);
        map.put("limpkin",8);
        map.put("European gallinule",8);
        map.put("American coot",8);
        map.put("bustard",8);
        map.put("ruddy turnstone",8);
        map.put("red-backed sandpiper",8);
        map.put("redshank",8);
        map.put("dowitcher",8);
        map.put("oystercatcher",8);
        map.put("pelican",8);
        map.put("king penguin",8);
        map.put("albatross",8);
        map.put("grey whale",8);
        map.put("killer whale",8);
        map.put("dugong",8);
        map.put("sea lion",8);
        map.put("Chihuahua",8);
        map.put("Japanese spaniel",8);
        map.put("Maltese dog",8);
        map.put("Pekinese",8);
        map.put("Shih-Tzu",8);
        map.put("Blenheim spaniel",8);
        map.put("papillon",8);
        map.put("toy terrier",8);
        map.put("Rhodesian ridgeback",8);
        map.put("Afghan hound",8);
        map.put("basset",8);
        map.put("beagle",8);
        map.put("bloodhound",8);
        map.put("bluetick",8);
        map.put("black-and-tan coonhound",8);
        map.put("Walker hound",8);
        map.put("English foxhound",8);
        map.put("redbone",8);
        map.put("borzoi",8);
        map.put("Irish wolfhound",8);
        map.put("Italian greyhound",8);
        map.put("whippet",8);
        map.put("Ibizan hound",8);
        map.put("Norwegian elkhound",8);
        map.put("otterhound",8);
        map.put("Saluki",8);
        map.put("Scottish deerhound",8);
        map.put("Weimaraner",8);
        map.put("Staffordshire bullterrier",8);
        map.put("American Staffordshire terrier",8);
        map.put("Bedlington terrier",8);
        map.put("Border terrier",8);
        map.put("Kerry blue terrier",8);
        map.put("Irish terrier",8);
        map.put("Norfolk terrier",8);
        map.put("Norwich terrier",8);
        map.put("Yorkshire terrier",8);
        map.put("wire-haired fox terrier",8);
        map.put("Lakeland terrier",8);
        map.put("Sealyham terrier",8);
        map.put("Airedale",8);
        map.put("Australian terrier",8);
        map.put("Dandie Dinmont",8);
        map.put("Boston bull",8);
        map.put("miniature schnauzer",8);
        map.put("giant schnauzer",8);
        map.put("standard schnauzer",8);
        map.put("Scotch terrier",8);
        map.put("Tibetan terrier",8);
        map.put("silky terrier",8);
        map.put("soft-coated wheaten terrier",8);
        map.put("West Highland white terrier",8);
        map.put("Lhasa",8);
        map.put("flat-coated retriever",8);
        map.put("curly-coated retriever",8);
        map.put("golden retriever",8);
        map.put("Labrador retriever",8);
        map.put("Chesapeake Bay retriever",8);
        map.put("German short-haired pointer",8);
        map.put("vizsla",8);
        map.put("English setter",8);
        map.put("Irish setter",8);
        map.put("Gordon setter",8);
        map.put("Brittany spaniel",8);
        map.put("clumber",8);
        map.put("English springer",8);
        map.put("Welsh springer spaniel",8);
        map.put("cocker spaniel",8);
        map.put("Sussex spaniel",8);
        map.put("Irish water spaniel",8);
        map.put("kuvasz",8);
        map.put("schipperke",8);
        map.put("groenendael",8);
        map.put("malinois",8);
        map.put("briard",8);
        map.put("kelpie",8);
        map.put("komondor",8);




    }

    public int getCategory() {  // 카테고리 번호 반환 0~8

        return map.get(product)+1;

    }
}