/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ProjectRoot;

public class TimeConverter {
    public static String timeConversion(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds - (hoursToSeconds(hours))) / 60;
        int seconds = totalSeconds - ((hoursToSeconds(hours)) + (minutesToSeconds(minutes)));

        if(hours<1000) return hours + " hours " + minutes + " minutes " + seconds + " seconds";
        else return "Unknown";
    }

    private static int hoursToSeconds(int hours) {
        return hours * 3600;
    }

    private static int minutesToSeconds(int minutes) {
        return minutes * 60;
    }
}
