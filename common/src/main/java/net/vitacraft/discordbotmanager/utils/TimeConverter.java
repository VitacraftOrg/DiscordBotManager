package net.vitacraft.discordbotmanager.utils;

public class TimeConverter {
    public static String millisecondsToPretty(double milliseconds) {
        if (milliseconds < 0) {
            return "§cN/A";
        }

        // Define time constants
        final int SECOND = 1000;
        final int MINUTE = 60 * SECOND;
        final int HOUR = 60 * MINUTE;
        final int DAY = 24 * HOUR;

        // Calculate the time units
        long days = (long) (milliseconds / DAY);
        milliseconds %= DAY;

        long hours = (long) (milliseconds / HOUR);
        milliseconds %= HOUR;

        long minutes = (long) (milliseconds / MINUTE);
        milliseconds %= MINUTE;

        long seconds = (long) (milliseconds / SECOND);

        // Build the pretty string
        StringBuilder prettyString = new StringBuilder();
        if (days > 0) {
            prettyString.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
        }
        if (hours > 0) {
            prettyString.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(", ");
        }
        if (minutes > 0) {
            prettyString.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(", ");
        }
        if (seconds > 0) {
            prettyString.append(seconds).append(" second").append(seconds != 1 ? "s" : "");
        }
        int length = prettyString.length();
        if (length > 0 && prettyString.charAt(length - 1) == ' ') {
            prettyString.delete(length - 2, length); // remove the trailing ", "
        }
        return prettyString.toString();
    }
}
