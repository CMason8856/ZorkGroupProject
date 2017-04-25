/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alfredo
 */
public class HelpCommand extends Command{
    HelpCommand() {}
    public String execute(){
    return "\n\t\tHere's a list of commands:"+
            "\n Drop (item name)"+
            "\n health"+
            "\n i or inventory"+
            "\n take (item name)"+
            "\n q or quit"+
            "\n save"+
            "\n score"+
            "\n examine (item name)"+
            "\n use (Key)"+
            "\n detonate (Bomb)"+
            "\n kick (Bomb,DrPepper, or emptyCan)"+
            "\n shake (DrPepper)"+
            "\n drink (DrPepper or emptyCan)"+
            "\n stomp (donut, emptyCan, or squishedCan)"+
            "\n break (magicWand or StarWarsToy)"+
            "\n wave (magicWand)"+
            "\n touch (StarWarsToy)"+
            "\n refill (WawaTravelMug)"+
            "\n eat (donut)\n"
            ;
    }
    
}
