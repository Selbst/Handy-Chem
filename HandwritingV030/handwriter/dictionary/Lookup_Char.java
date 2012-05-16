package dictionary;

public class Lookup_Char {
	public static String lookup(int index, boolean uppercase) {
		if (uppercase) {
			if (index == 0) {
				return "A";
			} else if (index == 1) {
				return "B";
			} else if (index == 2) {
				return "C";
			} else if (index == 3) {
				return "D";
			} else if (index == 4) {
				return "E";
			} else if (index == 5) {
				return "F";
			} else if (index == 6) {
				return "G";
			} else if (index == 7) {
				return "H";
			} else if (index == 8) {
				return "I";
			} else if (index == 9) {
				return "J";
			} else if (index == 10) {
				return "K";
			} else if (index == 11) {
				return "L";
			} else if (index == 12) {
				return "M";
			} else if(index == 13) {
				return "N";
			} else if (index == 14) {
				return "O";
			} else if (index == 15) {
				return "P";
			} else if (index == 16) {
				return "Q";
			} else if (index == 17) {
				return "R";
			} else if (index == 18) {
				return "S";
			} else if (index == 19) {
				return "T";
			} else if (index == 20) {
				return "U";
			} else if (index == 21) {
				return "V";
			} else if (index == 22) {
				return "W";
			} else if (index == 23) {
				return "X";
			} else if (index == 24) {
				return "Y";
			} else if (index == 25) {
				return "Z";
			} else {
				return "";
			}		
		} else {
			if (index == 0) {
				return "a";
			} else if (index == 1) {
				return "b";
			} else if (index == 2) {
				return "c";
			} else if (index == 3) {
				return "d";
			} else if (index == 4) {
				return "e";
			} else if (index == 5) {
				return "f";
			} else if (index == 6) {
				return "g";
			} else if (index == 7) {
				return "h";
			} else if (index == 8) {
				return "i";
			} else if (index == 9) {
				return "j";
			} else if (index == 10) {
				return "k";
			} else if (index == 11) {
				return "l";
			} else if (index == 12) {
				return "m";
			} else if(index == 13) {
				return "n";
			} else if (index == 14) {
				return "o";
			} else if (index == 15) {
				return "p";
			} else if (index == 16) {
				return "q";
			} else if (index == 17) {
				return "r";
			} else if (index == 18) {
				return "s";
			} else if (index == 19) {
				return "t";
			} else if (index == 20) {
				return "u";
			} else if (index == 21) {
				return "v";
			} else if (index == 22) {
				return "w";
			} else if (index == 23) {
				return "x";
			} else if (index == 24) {
				return "y";
			} else if (index == 25) {
				return "z";
			} else {
				return "";
			}
		}
	}
	public static int convert_lowercase_index(String string) {
		if (Integer.parseInt(string) < 11) {
			return Integer.parseInt(string);
		} else {
			return Integer.parseInt(string)+1;
		}
	}
}
