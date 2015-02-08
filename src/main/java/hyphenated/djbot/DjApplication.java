package hyphenated.djbot;

import hyphenated.djbot.health.ChannelCheck;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.File;
import java.io.PrintStream;

public class DjApplication extends Application<DjConfiguration> {

    public static void main(String[] args) {
        try {
            new DjApplication().run(args);
        } catch (Exception e) {
            try {
                File file = new File("logs/errorFromMain.txt");
                PrintStream ps = new PrintStream(file);
                e.printStackTrace(ps);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return "DjBot";
    }

    @Override
    public void initialize(Bootstrap<DjConfiguration> bootstrap) {

        bootstrap.addBundle(new AssetsBundle("/assets", "/djbot/ui", "index.html"));
    }

    @Override
    public void run(DjConfiguration configuration,
                    Environment environment) {
        DjResource resource = new DjResource(configuration);
        environment.jersey().register(resource);

        final ChannelCheck channelCheck = new ChannelCheck(resource);

        environment.healthChecks().register("currentSong", channelCheck);
    }
}
