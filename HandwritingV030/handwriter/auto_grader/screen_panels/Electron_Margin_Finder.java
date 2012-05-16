package auto_grader.screen_panels;

public class Electron_Margin_Finder {
	public static int get_margin_right(String symbol) {
		int margin = 0;
		
		char first_char = symbol.charAt(0);
		if (first_char == 'A' || first_char == 'B' || first_char == 'C' || first_char == 'D' || first_char == 'E'
			|| first_char == 'F' || first_char == 'G'	 || first_char == 'H' || first_char == 'K' || first_char == 'N'
				|| first_char == 'O' || first_char == 'P' || first_char == 'Q' || first_char == 'R' || first_char == 'S' 
					|| first_char == 'T' || first_char == 'U' || first_char == 'V' || first_char == 'X' || first_char == 'Y'
						|| first_char == 'Z'
							|| first_char == 'I' || first_char == 'L') {
			margin = margin + 10;
		} else if (first_char == 'M' || first_char == 'W') {
			margin = margin + 12;
		}
		
		if (symbol.length() == 2) {
			char second_char = symbol.charAt(1);
			
			if (second_char == 'a' || second_char == 'b' || second_char == 'c' || second_char == 'd' || second_char == 'e'
				|| second_char == 'f' || second_char == 'g'	 || second_char == 'h' || second_char == 'k' || second_char == 'n'
					|| second_char == 'o' || second_char == 'p' || second_char == 'q' || second_char == 'r' || second_char == 's' 
						|| second_char == 't' || second_char == 'u' || second_char == 'v' || second_char == 'x' || second_char == 'y'
							|| second_char == 'z') {
				margin = margin + 7;
				
			} else if (second_char == 'i' || second_char == 'l') {
				margin = margin + 6;
			} else if (second_char == 'm' || second_char == 'w') {
				margin = margin + 12;
			}
			
		} else if (symbol.length() == 3) {
			
		}
		return margin;
	}
	public static int get_margin_top_bottom(String symbol) {
		int margin = 0;
		
		char first_char = symbol.charAt(0);
		if (first_char == 'A' || first_char == 'B' || first_char == 'C' || first_char == 'D' || first_char == 'E'
			|| first_char == 'F' || first_char == 'G'	 || first_char == 'H' || first_char == 'K' || first_char == 'N'
				|| first_char == 'O' || first_char == 'P' || first_char == 'Q' || first_char == 'R' || first_char == 'S' 
					|| first_char == 'T' || first_char == 'U' || first_char == 'V' || first_char == 'X' || first_char == 'Y'
						|| first_char == 'Z'
							|| first_char == 'I' || first_char == 'L') {
			margin = margin + 3;
		} else if (first_char == 'M' || first_char == 'W') {
			margin = margin + 5;
		}
		
		if (symbol.length() == 2) {
			char second_char = symbol.charAt(1);
			
			if (second_char == 'a' || second_char == 'b' || second_char == 'c' || second_char == 'd' || second_char == 'e'
				|| second_char == 'f' || second_char == 'g'	 || second_char == 'h' || second_char == 'k' || second_char == 'n'
					|| second_char == 'o' || second_char == 'p' || second_char == 'q' || second_char == 'r' || second_char == 's' 
						|| second_char == 't' || second_char == 'u' || second_char == 'v' || second_char == 'x' || second_char == 'y'
							|| second_char == 'z') {
				margin = margin + 4;
				
			} else if (second_char == 'i' || second_char == 'l') {
				margin = margin + 3;
			} else if (second_char == 'm' || second_char == 'w') {
				margin = margin + 6;
			}
			
		} else if (symbol.length() == 3) {
			
		}
		return margin;
	}
}
