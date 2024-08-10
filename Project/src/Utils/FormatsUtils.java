package Utils;

public class FormatsUtils {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_CYAN_BOLD = "\033[1;36m";
	public static final String ANSI_YELLOW_UNDERLINED = "\033[4;33m";
	public static final String ANSI_YELLOW_BOLD = "\033[1;33m";
	public static final String ANSI_CYAN_BRIGHT = "\033[0;96m";
	public static final String ANSI_RED_BRIGHT = "\033[0;91m"; 
	public static final String ANSI_GREEN_BRIGHT = "\033[0;92m";
	
	
	public static void printTitle(String msg, String color) {
		System.out.println(color + "--------------------------------------");
		System.out.println(msg);
		System.out.println("--------------------------------------" + ANSI_RESET);
	}
	
	public static void successMsg(String msg) {
		System.out.println(ANSI_GREEN_BRIGHT + msg + ANSI_RESET);
	}
	public static void failureMsg(String msg) {
		System.out.println(ANSI_RED_BRIGHT + msg + ANSI_RESET);
	}
}
