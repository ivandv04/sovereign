package legendev.sovereign.util;

import org.jetbrains.annotations.NotNull;

public final class RandomGen {

    private RandomGen() {
    }

    private static final String[] FIRST_NAMES = new String[]{
            "Lizet", "Skyler", "Kari", "Gary", "Tina", "Moriah", "Deangelo", "Dayana", "Lilian",
            "Baby", "Valencia", "Laisha", "Jose", "Haylee", "Armani", "Tatyana", "Tyrone", "Jonathon",
            "Dana", "Alysha", "Raymond", "Oliver", "Alexandra", "Koby", "Makiya", "Stella", "Kellie",
            "Lesly", "Ryland", "Clarence", "Lanie", "Mireya", "Johnny", "Anyssa", "Markus", "Kara",
            "Juan", "Maddie", "Allen", "Santana", "Mckinley", "Aysia", "Ty", "Jaycob", "Lilianna",
            "Tyreek", "Clayton", "Declan", "Anastasia", "Jenifer", "Estevan", "Alexis", "Aracely",
            "Kevon", "Charlie", "Ali", "Aubree", "Keyonna", "Clint", "Dane", "Arianna", "Cullen",
            "Bradley", "Alisa", "Maryjane", "Lora", "Ivan", "Marvin", "Allison", "Samantha", "Syed",
            "Skye", "Chana", "Colleen", "Lily", "Quentin", "Ronaldo", "Bailey", "Christian", "Lilly",
            "Tyshawn", "Augustus", "Kaleb", "Savion", "Jami", "Tavian", "Oswaldo", "Dillion", "Myles",
            "Rebeca", "Kavon", "Serina", "Wade", "Breann", "Zack", "Jeffery", "Janell", "Daylon",
            "Weston", "Erick"
    };

    private static final String[] LAST_NAMES = new String[]{
            "Pfeifer", "Juarez", "Searcy", "Maddox", "Lange", "McCallum", "Houser", "Hannon", "Teague",
            "Rossi", "Whittaker", "Lennon", "Ramey", "Mackay", "Hare", "Baumann", "Grabowski", "Potter",
            "Graber", "Coe", "Rosado", "Kimmel", "Rooney", "Herron", "Wolfe", "Greenwood", "Hale",
            "Windham", "Harlan", "Westbrook", "Montero", "Brewer", "Neill", "Newsom", "Dyson", "Carlson",
            "Bianco", "Webster", "Chapin", "Shell", "Kish", "Strong", "Herndon", "Caballero", "Bernstein",
            "Bellamy", "Bartley", "Chance", "Duncan", "McArthur", "Atwood", "Corley", "Dinh", "Gillis",
            "Cash", "Watts", "Holly", "Gresham", "Wilks", "Reimer", "Rao", "Randolph", "Staples", "Floyd",
            "Carroll", "Seaton", "Elam", "Overstreet", "Van", "Wilcox", "Wylie", "Gant", "Hancock",
            "Hollis", "Gallo", "Bush", "Wegner", "Pierce", "Palumbo", "Tuttle", "Kozlowski", "Munn",
            "Rowley", "Abbott", "Still", "Vigil", "Winstead", "Griffiths", "Brand", "Bowman", "Olvera",
            "Yoo", "Abernathy", "Bratton", "Briones", "Hackett", "Hinds", "Ellis", "Chang", "Call"
    };

    private static final String[] ADJECTIVES = new String[]{
            "Bent", "Wretched", "Imminent", "Enthusiastic", "Secretive", "Overconfident", "Fluffy",
            "Chemical", "Striped", "Straight", "Nebulous", "Tasteful", "Gifted", "Magnificent", "Gainful",
            "Guarded", "Greasy", "Next", "Brainy", "Verdant", "Spotless", "Regular", "Nostalgic", "Lacking",
            "Vengeful", "Unable", "Fresh", "Economic", "Uneven", "Distinct", "Brawny", "Exuberant",
            "Important", "Aquatic", "Possible", "Attractive", "Awake", "Befitting", "One", "Wise",
            "Shocking", "Garrulous", "Premium", "Spotty", "Crazy", "Honorable", "Wrathful", "Spurious",
            "Medical", "Great", "Chivalrous", "Ignorant", "Actually", "Drab", "Jumpy", "Symptomatic",
            "Warlike", "Adaptable", "Measly", "Broad", "Nappy", "Shivering", "Pretty", "Homeless", "Deeply",
            "Typical", "Sable", "Mammoth", "Productive", "Pink", "Subdued", "Barbarous", "Hallowed",
            "Clammy", "Obscene", "Omniscient", "Stormy", "Ratty", "Overrated", "Unwritten", "Physical",
            "Lackadaisical", "Wicked", "Legal", "Plain", "Lively", "Lush", "Hard", "Frequent", "Hungry",
            "Defeated", "Unhealthy", "Scared", "Demonic", "Axiomatic", "Astonishing", "Ill", "Accessible",
            "Imperfect", "Phobic", "Selfish", "Vigorous", "Hellish", "Dead", "Fumbling", "Fluttering",
            "Skinny", "Outstanding", "Lying", "Alleged", "Innocent", "Wary", "Apathetic", "Ossified",
            "Wistful", "Taboo", "Ugliest", "Puny", "Tricky", "Bumpy", "Roasted", "Cute", "Dear", "Careless",
            "Separate", "Three", "Shaggy", "Nippy", "Nutritious", "Standing", "Jittery", "Sulky", "Useful",
            "Hurt", "Truculent", "Grumpy", "Misty", "Obese", "Synonymous", "Certain", "Ambiguous",
            "Adorable", "Natural", "Steadfast", "Heartbreaking", "Yummy", "Political", "Grieving", "Hollow",
            "Waggish", "Lyrical", "Quarrelsome", "Ordinary", "Outrageous", "Eminent", "Aromatic", "Acidic",
            "Obedient", "Past", "Vacuous", "Uninterested", "Tart", "Salty", "Rambunctious", "Picayune",
            "Savory", "Smoggy", "Panicky", "Obeisant", "Acid", "Smooth", "Probable", "Plausible", "Cruel",
            "Furtive", "Worthless", "Macabre", "Perfect", "Evasive", "Deep", "Rotten", "Narrow", "Icky",
            "Quickest", "Present", "Familiar", "Offbeat", "Plastic", "Undesirable", "Murky", "Harsh",
            "Absurd", "Disgusting", "Anxious", "Yellow", "Mountainous", "Resolute", "Abject", "Unused",
            "Enormous"
    };

    public static @NotNull String randomName() {
        int fL = FIRST_NAMES.length;
        int lL = LAST_NAMES.length;
        String sp = " ";
        String out = FIRST_NAMES[(int) (Math.random() * fL)];
        if (Math.random() < 0.1) out += sp + FIRST_NAMES[(int) (Math.random() * fL)];
        if (Math.random() < 0.01) out += sp + LAST_NAMES[(int) (Math.random() * lL)];
        out += sp + LAST_NAMES[(int) (Math.random() * lL)];
        return out;
    }

    public static @NotNull String randomNameShort() {
        return FIRST_NAMES[(int) (Math.random() * FIRST_NAMES.length)]
                + " " + LAST_NAMES[(int) (Math.random() * LAST_NAMES.length)];
    }

    public static @NotNull String randomTitledName() {
        return FIRST_NAMES[(int) (Math.random() * FIRST_NAMES.length)]
                + " the " + ADJECTIVES[(int) (Math.random() * ADJECTIVES.length)];
    }

}
