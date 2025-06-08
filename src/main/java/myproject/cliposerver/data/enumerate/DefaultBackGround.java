package myproject.cliposerver.data.enumerate;

import lombok.Getter;

import java.util.Random;

@Getter
public enum DefaultBackGround {
    BG_DEFAULT_1("bg_default_1"),
    BG_DEFAULT_2("bg_default_2"),
    BG_DEFAULT_3("bg_default_3"),
    BG_DEFAULT_4("bg_default_4"),
    BG_DEFAULT_5("bg_default_5"),
    BG_DEFAULT_6("bg_default_6");

    private final String profileImage;

    DefaultBackGround(String profileImage) {
        this.profileImage = profileImage;
    }

    private static final Random RANDOM = new Random();

    public static String getRandomBackGroundImage() {
        DefaultBackGround[] values = DefaultBackGround.values();
        int randomIndex = RANDOM.nextInt(values.length);
        return values[randomIndex].profileImage;
    }
}
