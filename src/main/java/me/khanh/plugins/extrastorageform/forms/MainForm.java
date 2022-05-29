package me.khanh.plugins.extrastorageform.forms;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import lombok.Getter;
import me.khanh.plugins.extrastorageform.forms.contents.Button;
import me.khanh.plugins.extrastorageform.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainForm{

    @Getter
    private final Section section;

    @Getter
    private final String title;

    @Getter
    private final boolean load;


    @Getter
    List<Button> buttons = new ArrayList<>();

    public MainForm(Section section){
        this.section = section;

        if (section.get("Title") == null){
            Logger.info(String.format("WARN: %s.%s is null. Use default value ('')", section.getRouteAsString(), "Title"));
            title = "";
        } else {
            title = section.getString("Title");
        }

        Section buttonSection = section.getSection("Contents");
        for (Object obj : buttonSection.getKeys()){
            String key = String.valueOf(obj);
            String[] arrayOfButton = new String[]{"Storage", "Filter", "Partner", "Sell", "Whitelist"};
            if (!Arrays.asList(arrayOfButton).contains(key)){
                Logger.info(String.format("WARN %s.%s what is this?. Please delete this", buttonSection.getRouteAsString(), key));
                continue;
            }
            buttons.add(new Button(buttonSection.getSection(key)));
        }
        load = true;
    }

    public SimpleForm buildForm(Player player){
        List<ButtonComponent> buttonComponents = new ArrayList<>();
        for (Button button : buttons){
            buttonComponents.add(button.build(player));
        }
        for (ButtonComponent component : buttonComponents){
            Logger.info(component.getText());
        }
        return SimpleForm.of(Logger.placeholder(player, title), "", buttonComponents);
    }

    public void open(FloodgatePlayer player){
        Player bukkitPlayer = Bukkit.getPlayer(player.getJavaUsername());
        player.sendForm(buildForm(bukkitPlayer));
    }

}