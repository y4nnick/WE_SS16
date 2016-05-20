package at.ac.tuwien.big.we16.ue3.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Date;

/**
 * Created by mstrasser on 5/20/16.
 * TODO: Fix logging
 */
public class TwitterService {
    private String twitterUrl = "https://twitter.com/BIGEWA2013";
    private String consumerKey = "GZ6tiy1XyB9W0P4xEJudQ";
    private String consumerSecret = "gaJDlW0vf7en46JwHAOkZsTHvtAiZ3QUd2mD1x26J9w";
    private String accessToken = "1366513208-MutXEbBMAVOwrbFmZtj1r4Ih2vcoHGHE2207002";
    private String accessTokenSecret = "RMPWOePlus3xtURWRVnv1TgrjTyK7Zk33evp4KKyA";
    private String from = "API Tester";
    private Twitter twitter = null;
    private static final Logger logger = LogManager.getLogger("TwitterService");

    public TwitterService() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(this.consumerKey)
                .setOAuthConsumerSecret(this.consumerSecret)
                .setOAuthAccessToken(this.accessToken)
                .setOAuthAccessTokenSecret(this.accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        this.twitter = tf.getInstance();
    }

    /**
     * Publishes an entry on the BIG Board on Twitter.
     * @param id The ID of the BIG Board entry.
     * @return The tweets content.
     */
    public String postToTwitter(String id) {
        TwitterStatusMessage msg = new TwitterStatusMessage(this.from, id, new Date());
        try {
            Status status = this.twitter.updateStatus(msg.getTwitterPublicationString());
            return msg.getTwitterPublicationString();
        } catch (IllegalStateException e) {
            TwitterService.logger.error("Something went wrong. Are your Twitter credentials set?");
        } catch (TwitterException e) {
            TwitterService.logger.error("Error publishing tweet.");
        }

        return null;
    }
}
