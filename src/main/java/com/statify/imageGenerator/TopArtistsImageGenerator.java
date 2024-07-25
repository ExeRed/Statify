package com.statify.imageGenerator;

import com.statify.model.Artist;
import com.statify.model.Track;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class TopArtistsImageGenerator {

    private static final int IMAGE_WIDTH = 1600; // Adjust as needed
    private static final int IMAGE_HEIGHT = 2450; // Adjust as needed

    public String generateBase64Image(String userName, List<Artist> artists, String timePeriod, String format) {
        // Create a new BufferedImage object
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Create a Graphics2D object for drawing on the image
        Graphics2D graphics = image.createGraphics();

        // Set font and colors
        Font titleFont = new Font("Inter", Font.PLAIN, 72);
        Font artistFont = new Font("Inter", Font.BOLD, 56);
        Font usernameFont = new Font("Inter", Font.BOLD, 96);
        Color whiteColor = Color.WHITE;
        Color grayColor = Color.decode("#CDCDCD");
        Color statifyColor = Color.decode("#1DB954");

        // Draw title
        graphics.setFont(titleFont);
        graphics.setColor(whiteColor);
        graphics.drawString("Top Artists", 611, 120);

        // Draw username
        graphics.setFont(usernameFont);
        graphics.setColor(statifyColor);
        graphics.drawString(userName, 150, 247);

        // Draw timePeriod
        graphics.setFont(titleFont);
        graphics.setColor(whiteColor);
        graphics.drawString(timePeriod, 992, 247);

        // Draw top 10 artists
        int y = 340;
        for (int i = 0; i < Math.min(artists.size(), 10); i++) {
            Artist artist = artists.get(i);

            // Draw artist number
            graphics.setFont(artistFont);
            graphics.setColor(whiteColor);
            graphics.drawString("#" + (i + 1), 95, y + 95);

            // Draw artist cover
            try {
                URL url = new URL(artist.getImages().get(0).getUrl());
                BufferedImage albumImage = ImageIO.read(url);
                graphics.drawImage(albumImage, 230, y, 150, 150, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Draw artist
            graphics.setFont(artistFont);
            graphics.setColor(whiteColor);
            graphics.drawString(artist.getName(), 410, y + 103);

            y += 190;
        }

        BufferedImage logoImage = null;
        try (InputStream is = getClass().getResourceAsStream("/static/images/statify-logo.png")) {
            if (is != null) {
                logoImage = ImageIO.read(is);
            } else {
                throw new IOException("Image not found in resources");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle image loading error
        }

        graphics.drawImage(logoImage, 95, y + 40, 150, 86, null);
        graphics.setFont(artistFont);
        graphics.setColor(whiteColor);
        graphics.drawString("t.me/statify_sbot/statify", 290, y + 101);

        // Dispose of the Graphics2D object
        graphics.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, baos);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle image writing error
            return null;
        }
        byte[] imageBytes = baos.toByteArray();

        // Encode byte array to Base64
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);

        return base64EncodedImage;
    }
}