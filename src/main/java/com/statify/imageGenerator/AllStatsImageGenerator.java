package com.statify.imageGenerator;

import com.statify.model.Artist;
import com.statify.model.Track;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class AllStatsImageGenerator {
    private static final int IMAGE_WIDTH = 1600; // Adjust as needed
    private static final int IMAGE_HEIGHT = 2450; // Adjust as needed

    public String generateBase64Image(String userName, List<Artist> topArtists, List<Track> topTracks, List<String> topGenres, String timePeriod, String format) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Background color
        graphics.setColor(Color.decode("#121212"));
        graphics.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // Colors and fonts
        Color whiteColor = Color.WHITE;
        Color grayColor = Color.decode("#CDCDCD");
        Color statifyColor = Color.decode("#1DB954");
        Font usernameFont = new Font("Inter", Font.BOLD, 96);
        Font timePeriodFont = new Font("Inter", Font.PLAIN, 76);
        Font titleFont = new Font("Inter", Font.BOLD, 56);
        Font contentFont = new Font("Inter", Font.PLAIN, 48);

        // Draw Username
        graphics.setColor(statifyColor);
        graphics.setFont(usernameFont);

        // Limit username to a maximum of 25 characters and add ellipsis if truncated
        String displayName = userName.length() > 25 ? userName.substring(0, 22) + "..." : userName + "'s";

        // Calculate the width of the username text for centering
        FontMetrics usernameMetrics = graphics.getFontMetrics(usernameFont);
        int usernameWidth = usernameMetrics.stringWidth(displayName);

        // Calculate the X-coordinate to center the text
        int centerX = (IMAGE_WIDTH - usernameWidth) / 2;

        // Draw the centered username
        graphics.drawString(displayName, centerX, 250);

        // Draw Time Period
        graphics.setColor(whiteColor);
        graphics.setFont(timePeriodFont);
        graphics.drawString("stats for past " + timePeriod, 400, 350);

        int[] xPositions = {350, 850, 575}; // X-coordinates for each artist image
        int[] yPositions = {500, 650, 950}; // Y-coordinates for each artist image
        int[] sizes = {450, 400, 350};      // Diameter (width and height) for each artist image

        for (int i = 0; i < Math.min(topArtists.size(), xPositions.length); i++) {
            try {
                // Fetch artist image from URL
                URL url = new URL(topArtists.get(i).getImages().get(0).getUrl());
                BufferedImage artistImage = ImageIO.read(url);

                // Create a circular cropped version of the artist image
                BufferedImage circleImage = new BufferedImage(sizes[i], sizes[i], BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = circleImage.createGraphics();

                // Enable anti-aliasing for smooth edges
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create a circular clip
                g2d.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, sizes[i], sizes[i]));

                // Draw the artist image within the circular clip
                g2d.drawImage(artistImage, 0, 0, sizes[i], sizes[i], null);
                g2d.dispose();

                // Draw the circular image onto the main graphics context at specified position
                graphics.drawImage(circleImage, xPositions[i], yPositions[i], null);
            } catch (IOException e) {
                e.printStackTrace();
                // If image loading fails, draw a gray circle as a placeholder
                graphics.setColor(grayColor);
                graphics.fillOval(xPositions[i], yPositions[i], sizes[i], sizes[i]);
            }
        }



// Set maximum character length for song and artist names
        int maxTitleLength = 20; // Adjust this value as needed

// Draw Top Songs List
        graphics.setColor(statifyColor);
        graphics.setFont(titleFont);
        graphics.drawString("top songs", 150, 1500);

        graphics.setColor(whiteColor);
        graphics.setFont(contentFont);
        int yPosition = 1600;
        for (int i = 0; i < Math.min(topTracks.size(), 5); i++) {
            String songName = topTracks.get(i).getName();

            // Truncate song name if it exceeds max length
            if (songName.length() > maxTitleLength) {
                songName = songName.substring(0, maxTitleLength - 3) + "...";
            }

            graphics.drawString((i + 1) + ". " + songName, 150, yPosition);
            yPosition += 70;
        }

// Draw Top Artists List
        graphics.setFont(titleFont);
        graphics.setColor(statifyColor);
        graphics.drawString("top artists",  1000, 1500);

        graphics.setColor(whiteColor);
        graphics.setFont(contentFont);
        yPosition = 1600;
        for (int i = 0; i < Math.min(topArtists.size(), 5); i++) {
            String artistName = topArtists.get(i).getName();

            // Truncate artist name if it exceeds max length
            if (artistName.length() > maxTitleLength) {
                artistName = artistName.substring(0, maxTitleLength - 3) + "...";
            }

            graphics.drawString( (i + 1) + ". " + artistName, 1000, yPosition);
            yPosition += 70;
        }


        // Draw Genres List
        graphics.setFont(titleFont);

// Limit to top 5 genres
        List<String> limitedGenres = topGenres.subList(0, Math.min(topGenres.size(), 5));
        String genreText = String.join(" • ", limitedGenres);

// Calculate the width of the genre text for centering
        FontMetrics fontMetrics = graphics.getFontMetrics(titleFont);
        int textWidth = fontMetrics.stringWidth(genreText);

// Calculate the X-coordinate to center the text
        centerX = (IMAGE_WIDTH - textWidth) / 2; // Assuming IMAGE_WIDTH is the width of the entire image

// Draw the centered genre text
        graphics.drawString(genreText, centerX, yPosition + 200);



        // Draw Statify Logo
        BufferedImage logoImage = null;
        try {
            logoImage = ImageIO.read(new File("src/main/resources/static/images/statify-logo.png"));
            graphics.drawImage(logoImage, 100, 2300, 150, 86, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.setFont(contentFont);
        graphics.drawString("statify.live", 300, 2350);


        // Dispose graphics
        graphics.dispose();

        // Convert image to Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, baos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
