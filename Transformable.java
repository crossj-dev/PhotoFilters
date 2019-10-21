/*
 * CS1021-081
 * Winter 2018-2019
 * File header contains class Transformable
 * Name: crossj
 * Created 2/7/2019
 */
package LabNine.LabNine;

import javafx.scene.paint.Color;

/**
 * CS1021-081 Winter 2018-2019
 * Class purpose: interface
 *
 * @author crossj
 * @version created on 2/7/2019 at 5:22 PM
 */
@FunctionalInterface
interface Transformable {
        Color apply(int y, Color color);

}
