package xyz.pandawan.autobackup;

/**
 * 
 * @author Pandawan
 * This basically detects the OS
 *
 */
public final class OsUtils {
	
   private static String OS = null;
   
   public static String getOsName()
   {
      if(OS == null) { 
    	  OS = System.getProperty("os.name"); 
      }
      return OS;
   }
   public static boolean isWindows () {
      return getOsName().startsWith("Windows");
   }

   public static boolean isLinux() {
	   return getOsName().startsWith("Linux");
   }
   
   public static boolean isMac() {
	   return getOsName().startsWith("Mac");
   }
   
	static String MakeWinPath (String input) {
		return input.replace("/", "\\");
	}
}
