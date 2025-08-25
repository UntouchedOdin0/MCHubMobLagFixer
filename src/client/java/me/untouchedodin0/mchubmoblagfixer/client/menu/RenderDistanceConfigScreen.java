package me.untouchedodin0.mchubmoblagfixer.client.menu;

import me.untouchedodin0.mchubmoblagfixer.client.MCHubMobLagFixerConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class RenderDistanceConfigScreen extends Screen {

    private int renderDistance = 128; // default value
    private final Screen parent;      // parent screen to return to

    public RenderDistanceConfigScreen(Screen parent) {
        super(Text.literal("Render Distance Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Slider for render distance in blocks
        this.addDrawableChild(new SliderWidget(
                centerX - 100, centerY - 20, 200, 20,
                Text.literal("Render Distance: " + MCHubMobLagFixerConfig.getRenderDistance() + " blocks"),
                MCHubMobLagFixerConfig.getRenderDistance() / 512.0) { // value is 0.0–1.0

            @Override
            protected void updateMessage() {
                int distance = (int) (this.value * 512);
                this.setMessage(Text.literal("Render Distance: " + distance + " blocks"));
            }

            @Override
            protected void applyValue() {
                int distance = (int) (this.value * 512);
                MCHubMobLagFixerConfig.setRenderDistance(distance);
            }
        });

/*        this.addDrawableChild(new SliderWidget(
                centerX - 100, centerY - 20, 200, 20,
                Text.literal("Render Distance: " + MCHubMobLagFixerConfig.getRenderDistance() + " blocks"),
                0.0, 1.0, MCHubMobLagFixerConfig.getRenderDistance() / 512.0, false,
                slider -> {
                    // `slider.getValue()` is actually `slider.getValue()` in old versions, but in modern versions use `slider.getValue()` is protected
                    // Instead, use slider.getValue() via the method `slider.value` (the public field)
                    int distance = (int) (slider * 512); // Max 512 blocks
                    MCHubMobLagFixerConfig.setRenderDistance(distance);
                }) {
            @Override
            protected void updateMessage() {

            }

            @Override
            protected void applyValue() {

            }
        });*/

/*        // Increase button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Increase Render Distance"),
                        button -> {
                            renderDistance += 16;
                            if (renderDistance > 512) renderDistance = 512;
                            button.setMessage(Text.literal("Render Distance: " + renderDistance));
                        })
                .dimensions(centerX - 100, centerY - 20, 200, 20)
                .build());

        // Decrease button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Decrease Render Distance"),
                        button -> {
                            renderDistance -= 16;
                            if (renderDistance < 16) renderDistance = 16;
                            button.setMessage(Text.literal("Render Distance: " + renderDistance));
                        })
                .dimensions(centerX - 100, centerY + 10, 200, 20)
                .build());*/

        // Back button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"),
                        button -> {
                            if (this.client != null) {
                                this.client.setScreen(parent);
                            }
                        })
                .dimensions(centerX - 100, centerY + 50, 200, 20)
                .build());
    }

    public int getRenderDistance() {
        return renderDistance;
    }
}
