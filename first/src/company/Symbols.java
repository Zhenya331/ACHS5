package company;

public class Symbols {
    private static final String s = " ,.;:-—(){}[]=!@#$%^&*¹?/|";

    private static final String[] spec = new String[] {
        "\n",
        "\t",
        "\r"
    };

    public static boolean Equals(char c) {
        for (int i = 0; i < s.length(); i++) {
            if (c == s.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    public static String Spec(String text) {
        String res = text;
        for (String value : spec) {
            res = res.replace(value, "  ");
        }
        return res;
    }
}
