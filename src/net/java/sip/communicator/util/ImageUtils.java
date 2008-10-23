/*
 * SIP Communicator, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.util;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

/**
 * Utility methods for image manipulation.
 *
 * @author Sebastien Mazy
 * @author Yana Stamcheva
 */
public class ImageUtils
{
    private static final Logger logger = Logger.getLogger(ImageUtils.class);

    /**
     * Returns a scaled image fitting within the given bounds
     * while keeping the aspect ratio.
     *
     * @param image the image to scale
     * @param width maximum width of the scaled image
     * @param height maximum height of the scaled image
     * @return the scaled image
     */
    public static ImageIcon
        scaleIconWithinBounds(Image image, int width, int height)
    {
        ImageIcon scaledImage;
        int scaleHint = Image.SCALE_SMOOTH;
        double originalRatio =
            (double) image.getWidth(null) / image.getHeight(null);
        double areaRatio = (double) width / height;

        if(originalRatio > areaRatio)
        {
            scaledImage = new ImageIcon(image.
                    getScaledInstance(width, -1,scaleHint));
        }
        else
        {
            scaledImage = new ImageIcon(image.
                    getScaledInstance(-1, height, scaleHint));
        }
        return scaledImage;
    }

    /**
     * Creates a rounded avatar image.
     * 
     * @param avatarBytes The bytes of the initial avatar image.
     * 
     * @return The rounded corner image.
     */
    public static ImageIcon getScaledRoundedImage(  Image image,
                                                    int width,
                                                    int height)
    {
        BufferedImage destImage = null;

        ImageIcon scaledImage = ImageUtils.scaleIconWithinBounds(   image,
                                                                    width,
                                                                    height);

        destImage
            = new BufferedImage(scaledImage.getIconWidth(),
                                scaledImage.getIconHeight(),
                                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = destImage.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, 
                        scaledImage.getIconWidth(),
                        scaledImage.getIconHeight(),
                        10, 10);
        g.setComposite(AlphaComposite.SrcIn);

        g.drawImage(scaledImage.getImage(), 0, 0, null);

        return new ImageIcon(destImage);
    }

    /**
     * Creates a rounded corner scaled image.
     * 
     * @param imageBytes The bytes of the image to be scaled.
     * @param width The maximum width of the scaled image.
     * @param height The maximum height of the scaled image.
     * 
     * @return The rounded corner scaled image.
     */
    public static ImageIcon getScaledRoundedImage(  byte[] imageBytes,
                                                    int width,
                                                    int height)
    {
        if (imageBytes == null || imageBytes.length < 1)
            return null;

        ImageIcon imageIcon = null;

        try
        {
            InputStream in = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(in);

            imageIcon = getScaledRoundedImage(image, width, height);
        }
        catch (Exception e)
        {
            logger.error("Could not create image.", e);
        }

        return imageIcon;
    }
}