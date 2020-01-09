package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tweet {

    String id;
    String username;
    Date date;
    String tweetContent;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    GregorianCalendar gmt = new GregorianCalendar(TimeZone.getTimeZone("GMT"));


    public Tweet(String username, Date date) {
        this.username = username;
        this.date = date;
        gmt.setTime(date);

    }


    public Tweet(String username, Date date, String tweetContent) {
        this.username = username;
        this.date = date;
        this.tweetContent = tweetContent;
        gmt.setTime(date);

    }

    public Tweet(String id, String username, Date date, String tweetContent) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.tweetContent = tweetContent;
        gmt.setTime(date);

    }

    public Tweet() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashTag() {

        Pattern pattern = Pattern.compile("#[^\\s]+");
        Matcher matcher = pattern.matcher(tweetContent);
        if (matcher.find())
            return matcher.group(0);
        return null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public int getSemaine() {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

    }

    public int getJour() {
        return gmt.get(Calendar.DAY_OF_YEAR);
    }

    public int getMois() {
        return gmt.get(Calendar.MONTH);
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public String getTweetContent() {
        return tweetContent;
    }

    public void setTweetContent(String tweetContent) {
        this.tweetContent = tweetContent;
    }

    @Override
    public String toString() {
        return "models.Tweet{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", date=" + date +
                ", tweetContent='" + tweetContent + '\'' +
                '}';
    }


    public static Tweet fromString(String string) {
        Tweet tweet = new Tweet();
        String[] split = string.split("\t");
        int size = split.length;
        tweet.setId(split[0]);

        int indexdate = 2;
        try {
            do {
                try {
                    tweet.setDate(tweet.simpleDateFormat.parse(split[indexdate]));
                } catch (ParseException e) {
                    indexdate++;
                }
            } while (tweet.getDate() == null);
        } catch (Exception e) {
            return null;
        }
        tweet.setUsername(split[indexdate - 1]);
        tweet.gmt.setTime(tweet.getDate());
        indexdate++;
        String content = "";
        for (; indexdate < split.length; indexdate++)
            content += split[indexdate];

        tweet.setTweetContent(content);
        return tweet;
    }


}
