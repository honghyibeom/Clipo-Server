package myproject.cliposerver.data.enumerate;

import lombok.Getter;

import java.util.Random;

@Getter
public enum DefaultProfile {
    DEFAULT_1("default_1"),
    DEFAULT_2("default_2"),
    DEFAULT_3("default_3"),
    DEFAULT_4("default_4");

    private final String profileImage;

    DefaultProfile(String profileImage) {
        this.profileImage = profileImage;
    }

    private static final Random RANDOM = new Random();

    public static String getRandomProfileImage() {
        DefaultProfile[] values = DefaultProfile.values();
        int randomIndex = RANDOM.nextInt(values.length);
        return values[randomIndex].profileImage;
    }
}
