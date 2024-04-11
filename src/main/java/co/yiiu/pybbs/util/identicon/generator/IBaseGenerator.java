package co.yiiu.pybbs.util.identicon.generator;

import java.awt.*;

/**
 * Author: Bryant Hang
 * Date: 15/1/10
 * Time: aa2:43
 */
public interface IBaseGenerator {
    /**
     * ahashaaaaaaboolaa6*5aa
     *
     * @param hash
     * @return
     */
    public boolean[][] getBooleanValueArray(String hash);


    /**
     * aaaaaaa
     *
     * @return
     */
    public Color getBackgroundColor();


    /**
     * aaaaaaa
     *
     * @return
     */
    public Color getForegroundColor();
}
