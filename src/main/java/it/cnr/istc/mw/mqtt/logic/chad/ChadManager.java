/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.cnr.istc.mw.mqtt.logic.chad;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author sommovir
 */
public class ChadManager {
    private static ChadManager _instance = null;
    private final static String chadFace = "⠀⠀⠘⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡜⠀⠀⠀\n" +
"⠀⠀⠀⠑⡀⠀⠀⠀W E L C O M E⠀⠀ T O  ⠀⡔⠁⠀⠀⠀\n" +
"⠀⠀⠀⠀⠈⠢⢄  ⠀C H A D L A N D⠀ ⣀⠴⠊⠀⠀⠀⠀⠀\n" +
"⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⠀⢀⣀⣀⣀⣀⣀⡀⠤⠄⠒⠒⠒⠈⠈⠀⠀⠀⠀⠀⠀⠀⠀\n" +
"⠀⠀⠀⠀⠀⠀⠀⠘⣀⠄⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n" +
"⠀\n" +
"⣿⣿⣿⣿⣿⣿⣿⣿⡿⠿⠛⠛⠛⠋⠉⠈⠉⠉⠉⠉⠛⠻⢿⣿⣿⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⣿⡿⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⢿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⡏⣀⠀⠀⠀⠀⠀⠀⠀⣀⣤⣤⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠙⢿⣿⣿\n" +
"⣿⣿⣿⢏⣴⣿⣷⠀⠀⠀⠀⠀⢾⣿⣿⣿⣿⣿⣿⡆⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿\n" +
"⣿⣿⣟⣾⣿⡟⠁⠀⠀⠀⠀⠀⢀⣾⣿⣿⣿⣿⣿⣷⢢⠀⠀⠀⠀⠀⠀⠀⢸⣿\n" +
"⣿⣿⣿⣿⣟⠀⡴⠄⠀⠀⠀⠀⠀⠀⠙⠻⣿⣿⣿⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⣿\n" +
"⣿⣿⣿⠟⠻⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠶⢴⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⣿\n" +
"⣿⣁⡀⠀⠀⢰⢠⣦⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⣿⣿⣿⣿⣿⡄⠀⣴⣶⣿⡄⣿\n" +
"⣿⡋⠀⠀⠀⠎⢸⣿⡆⠀⠀⠀⠀⠀⠀⣴⣿⣿⣿⣿⣿⣿⣿⠗⢘⣿⣟⠛⠿⣼\n" +
"⣿⣿⠋⢀⡌⢰⣿⡿⢿⡀⠀⠀⠀⠀⠀⠙⠿⣿⣿⣿⣿⣿⡇⠀⢸⣿⣿⣧⢀⣼\n" +
"⣿⣿⣷⢻⠄⠘⠛⠋⠛⠃⠀⠀⠀⠀⠀⢿⣧⠈⠉⠙⠛⠋⠀⠀⠀⣿⣿⣿⣿⣿\n" +
"⣿⣿⣧⠀⠈⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠀⠀⠀⠀⢀⢃⠀⠀⢸⣿⣿⣿⣿\n" +
"⣿⣿⡿⠀⠴⢗⣠⣤⣴⡶⠶⠖⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡸⠀⣿⣿⣿⣿\n" +
"⣿⣿⣿⡀⢠⣾⣿⠏⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠛⠉⠀⣿⣿⣿⣿\n" +
"⣿⣿⣿⣧⠈⢹⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⡄⠈⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣴⣾⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⣿⣦⣄⣀⣀⣀⣀⠀⠀⠀⠀⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡄⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠙⣿⣿⡟⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇⠀⠁⠀⠀⠹⣿⠃⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⣿⣿⣿⣿⡿⠛⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⢐⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
"⣿⣿⣿⣿⠿⠛⠉⠉⠁⠀⢻⣿⡇⠀⠀⠀⠀⠀⠀⢀⠈⣿⣿⡿⠉⠛⠛⠛⠉⠉\n" +
"⣿⡿⠋⠁⠀⠀⢀⣀⣠⡴⣸⣿⣇⡄⠀⠀⠀⠀⢀⡿⠄⠙⠛⠀⣀⣠⣤⣤⠄";
    
    
    private final static String chad = "                                                                                                                                                      \n" +
"                               `-:///////::-.`                                                      ```````                                           \n" +
"                      `-+yhddhdmmNNNNNNNNNNNNmds.                                          ..::/+++ooooooooooooooooooooo+                             \n" +
"                  `...````/dNNmNmNNNNNNNNNNNMNNNh                                     `-:+ossssssssssssssssssssssssssssso                             \n" +
"                  `````..``.ymmmNNNNNNNNNNNNmmdNd.                                 `-+ossssssssssssssssssssssssssssssssso                             \n" +
"                  `````+hy``.NNmNNNNNNNNNNNm+`.s/s                              `-+osssssssssssssssssssssssssssssssssssso                             \n" +
"                   ```+mmm+``yNNNNNNNNNNNNNNm:` `+                            `-+sssssssssssso+//:----------------------.                             \n" +
"                   ```smmNh`.oNmmmmmmNNNNNNNmmhhhd                           -+sssssssssso/-.`..-:/+++++++++++++++++++++++++///:--.`                  \n" +
"                   ```/mmmd`-oNNmmmmmNNNNNmmmmmmmh                         .+ssssssssso/.`.-/+osssssssssssssssssssssssssssssssssssso+/-`              \n" +
"                    ``.ymmh.:oNNNNNNNNNNNNNNmNNmm:                        -osssssssso:``:+osssssssssssssssssssssssssssssssssssssssssssso/-`           \n" +
"                  ````::/o+-/yNNNNNNNNNNNNNNNNNm+`                       :sssssssso:``:ossssssssssssssooooooooooooooooooooosssssssssssssss+-`         \n" +
"             `/++ossssyhs/:/oyMMMMMMMMMMMMMMMmmy-`                      :ssssssss+. -osssssssssoo+:--.......................-:/+osssssssssss+.        \n" +
"             `+sysyyyhhhhhhyhydNNNMNNNNNNNmmmdhyhyo/.                  -ssssssss/``/sssssssss+:.```.--:::::::::::::::::::       `-/ossssssssss:       \n" +
"              `/ssooossyhhddmNNmmmmmmmmddhyyyyyhhhddhs/`              `osssssss/ `+ssssssss+. `-/oossssssssssssssssssssso          `:osssssssss/      \n" +
"             ```-syyyddyyhdddmmmmmmmmmmmmmNNNNNNNNdddddho.            :sssssss+` /ssssssss: `:ossssssssssssssssssssssssso            `/sssssssss:     \n" +
"           .--....odddmdsdNNNNNNNNNNNNNNNNNNNNNNNNNmddmdhdo`         `osssssss- .ssssssss: `+ssssssssssssssssssssssssssso              :sssssssso.    \n" +
"             ./oo:.+ddddmhdNNNNNNNNNNNNNNNNNNNNNNNNNNmddmmmm/        .ssssssso  /ssssssso  +ssssssss+//::::::::::::::::::               :ssssssss:    \n" +
"               .yho-:ydddmmNNNNNNNNNNNNNNNNNNNNNNNNNNNNmhdmmm/       /sssssss/ `osssssss- .ssssssso.                                     +ssssssss    \n" +
"                .dhs--ommmmmNNNNNNNNNNNNNNNNNNNNNNNNNNNNNddmm+       +sssssss- .ssssssss. /sssssss-                                      -ssssssss    \n" +
"                `+hdo..+dmmmmmmNNNNNNNNNNNNNNNNNNNNNmmmmmmmmd:       /sssssss: `osssssso` +sssssss.                                      `ssssssss    \n" +
"                `-shh/..+symmmmmNNNNNNmmmmmmmmmmmmmmmmdhyo:..`       .sssssss+  /sssssso` +sssssss.                                      `ssssssss    \n" +
"               ``-+ymo-`.:/omm+dmmmmmmmmmmmmmdddhyso/:.`....`        `osssssso. .sssssso` +sssssss.                                      `ssssssss    \n" +
"               ``-/yNs+.`.-/os.+ssssyyyyyssoo+/:-.........`.`         /sssssss/  /ssssso` +sssssss.                                      `ssssssss    \n" +
"               ``-/yNyo:...-:-.-//:://////////::::---...``-:          `osssssss: `+sssso` +sssssss.                                      `ssssssss    \n" +
"               `.-/sNho+-.---..-:::::::::::::------......:/-           -ssssssss- `/ssso` +sssssss.                                      `ssssssss    \n" +
"               `.:/smhs+/--:-..-::::::::::--------......o+/.            /ssssssss:` -oso` +sssssss.                                      `ssssssss    \n" +
"               `.:/ymhso+/::-.--:/::::::---------....`-ss//`             /ssssssss+. `:+` +sssssss.                                      `ssssssss    \n" +
"              ``.:/yhhso++/:---::/::::::------:-....`-syo+:               :ossssssss/. `  +sssssss.                                      `ssssssss    \n" +
"              ``.:/ysysoo++/::-:://::::-----:/+....`.osy+/.                .+sssssssss+-` ./oossss.                                      `ssssssss    \n" +
"              `.-:/s/ysso++/:::::::::::-------...```+oys//.                 `:ossssssssso/-.`.-:/+.                                      `ssssssss    \n" +
"          .   `.-:/+`osoo++/:::::::::::---......````/oy+/:                    `/ossssssssssso//--.`````````````````````````````````````  `ssssssss    \n" +
"          -. ``.-:o+ :ooo++/::::::::::---../s.````   `-.`                       `:ossssssssssssssssssoooooooooooooooooooooooooooooooooo- `ssssssss    \n" +
"          `+.``.-:o: `+o++//::::::::----...ys.```                                  ./osssssssssssssssssssssssssssssssssssssssssssssssss: `ssssssss    \n" +
"           -:-``.-/.  -::::-----.........``````                                       .-/osssssssssssssssssssssssssssssssssssssssssssss: `ssssssss    \n" +
"                  `                                                                       `.::++oosssssssssssssssssssssssssssssssssssss: `ssssssss    \n" +
"                                                                                                 ``````````````````````````````````````   ````````    \n" +
"                                                                                                                                                      \n" +
"                                                                                                                                                      \n" +
"                                                                                                                                                      \n" +
"                                                                                                                                                      \n" +
"                                                                                                                                                      \n" +
"                                                                                                                                                      \n" +
"           `.-----.`      .....`      `.------.`          ......                  `.-----.`     `....`     .....       `.....        ..........`      \n" +
"        `/hmNMMMMMNmy:    hNNNN:    .odmNMMMMNNdo.       /NNNNNm.               -sdNNMMMNmh+`   sNNNN/    .NNNNm      `dNNNNNo      -NNNNNNNNNmds-    \n" +
"       .dMMMMdyyhNMMMN/   hMMMM:   +NMMMNhyydMMMMd.     .NMMMMMMh              sMMMMNdhdNMMMd.  yMMMM/    .MMMMN      sMMMMMMM:     -MMMMMddmNMMMNo   \n" +
"      `mMMMM/    -s+/:-   hMMMM:  :MMMMd.    /o+/:`     dMMMyNMMM+            +MMMMh.   -dhyo:  yMMMMo....:MMMMN     :MMMNyMMMm`    -MMMMd  `-mMMMM:  \n" +
"      :MMMMh   :ooooooo`  hMMMM:  hMMMM/   ooooooo/    oMMMm`+MMMN-           dMMMM:            yMMMMMMMMMMMMMMN    .NMMM+`mMMMy    -MMMMd    +MMMMy  \n" +
"      :MMMMh   sMMMMMMM.  hMMMM:  hMMMM:  `MMMMMMMh   :MMMM+``dMMMm`          dMMMM-     ```    yMMMMNNNNNNMMMMN    hMMMm``/MMMM+   -MMMMd    /MMMMy  \n" +
"      `NMMMN-  .::oMMMM.  hMMMM:  +MMMMh`  :::dMMMh  `NMMMMmmmNMMMMy          oMMMMs    `dNmh+  yMMMM/    .MMMMN   oMMMMNmmmMMMMN-  -MMMMd   `hMMMM+  \n" +
"       :NMMMNyo+oyNMMMM.  hMMMM:   yMMMMms++shMMMMh  hMMMMmmmmmNMMMM/          yMMMMdssyNMMMN:  yMMMM/    .MMMMN  -MMMMNmmmmmMMMMm` -MMMMNyyhmMMMMh   \n" +
"        `smMMMMMMMMMms:   hMMMM:    :hNMMMMMMMMNh+. +MMMMs     `NMMMN.          /hMMMMMMMMNy.   yMMMM/    .MMMMN `mMMMN.     sMMMMy -MMMMMMMMMMNh/    \n" +
"           .:////:-`      -::::`      `-:////:.     -::::`      .::::.            `-:///:-`     .::::`    `::::- `::::-      `::::- `:::::::--`       \n" +
"                                                                                                                                                      \n" +
"                                                                                                                                                      \n" +
"                                                                                                                                                      \n" +
"                                                                                                                                                      \n" +
"                                  .-:///:-`     -::::::::::::.  `-----------.   .----.      .----. -::::::::::::.  `:----------.                      \n" +
"                                -hmNMMMMMNmy.   dNNNNNNNNNNNNs  +NNNNNNNNNNNmh- /NNNNh      dNNNN- hNNNNNNNNNNNNy  /NNNNNNNNNNNmh:                    \n" +
"                               .NMMMy:-+NMMNd`  dMMMMhsssssss/  +MMMMdoooyMMMMN` yMMMM/    +MMMM+  hMMMMhsssssss+  /MMMMmoooyMMMMN.                   \n" +
"                               -MMMMms+/++:--   dMMMM/.......   +MMMMy```.NMMMM. `mMMMN`  `NMMMh   hMMMM+.......   /MMMMh```.mMMMM-                   \n" +
"                                /dNMMMMMMNds-   dMMMMNNNNNNNN`  +MMMMNdddNMMMm+   -NMMMs  yMMMN.   hMMMMNNNNNNNN.  /MMMMNdddNMMMm+                    \n" +
"                                 `:+shdmMMMMN/  dMMMMhsssssss   +MMMMNdNMMMNs.     +MMMM--MMMM/    hMMMMhsssssss`  /MMMMNdNMMMNs.                     \n" +
"                               syhhh`  `+MMMMy  dMMMM:          +MMMMs .dMMMMy`     hMMMddMMMs     hMMMM/          /MMMMy .hMMMMh.                    \n" +
"                               +MMMMdo/+hMMMN:  dMMMMdhhhhhhhy  +MMMMs  `dMMMMd`    `mMMMMMMd`     hMMMMdhhhhhhhy  /MMMMy  `hMMMMm.                   \n" +
"                                :hNNMMMMMNNy-   dNNNNNNNNNNNNm  +NNNNs   `hNNNNh`    :NNNNNN-      hNNNNNNNNNNNNm  /NNNNy   `hNNNNd`                  \n" +
"                                  `.-:::-.      ..............  `....`    `.....`     ......       ..............  `.....     .....`                  \n" +
"                                                                                                                                                      ";
    
    public static ChadManager getInstance() {
        if (_instance == null) {
            _instance = new ChadManager();
            return _instance;
        } else {
            return _instance;
        }
    }
    
    private ChadManager() {
        super();
    }

    public static String getChad() {
        return chad;
    }
    
    
    public String getChadFace(){
        return chadFace;
    }
    
}
