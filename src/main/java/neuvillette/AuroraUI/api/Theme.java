package neuvillette.AuroraUI.api;

import neuvillette.AuroraUI.api.color.RGBColor;

public class Theme {
    //color
    public RGBColor color_base_100;
    public RGBColor color_base_200;
    public RGBColor color_base_300;
    public RGBColor color_base_content;

    public RGBColor color_primary;
    public RGBColor color_primary_content;

    public RGBColor color_secondary;
    public RGBColor color_secondary_content;

    public RGBColor color_accent;
    public RGBColor color_accent_content;

    public RGBColor color_neutral;
    public RGBColor color_neutral_content;

    public RGBColor color_info;
    public RGBColor color_info_content;

    public RGBColor color_success;
    public RGBColor color_success_content;

    public RGBColor color_warning;
    public RGBColor color_warning_content;

    public RGBColor color_error;
    public RGBColor color_error_content;

    //Radius
    //Boxes
    public int radius_box;
    //Fields
    public int radius_field;
    //Selectors
    public int radius_selector;

    //Sizes
    //xs: 6*base sm:8*base md:10*base lg:12*base xl:14*base
    //Fields
    public int size_field;
    //Selectors
    public int size_selector;

    //Border Width
    public int border;

    public Theme(){
        color_base_100 = RGBColor.Oklch(97, 0.014, 343.198);
        color_base_200 = RGBColor.Oklch(94, 0.028, 342.258);
        color_base_300 = RGBColor.Oklch(89, 0.061, 343.231);
        color_base_content = RGBColor.Oklch(52, 0.223, 3.958);
        color_primary = RGBColor.Oklch(97, 0.014, 343.198);
        color_primary_content = RGBColor.Oklch(100, 0, 0);
        color_secondary = RGBColor.Oklch(62, 0.265, 303.9);
        color_secondary_content = RGBColor.Oklch(97, 0.014, 308.299);
        color_accent = RGBColor.Oklch(82, 0.111, 230.318);
        color_accent_content = RGBColor.Oklch(39,  0.09, 240.876);
        color_neutral = RGBColor.Oklch(40, 0.153, 2.432);
        color_neutral_content = RGBColor.Oklch(89, 0.061, 343.231);
        color_info = RGBColor.Oklch(86, 0.127, 207.078);
        color_info_content = RGBColor.Oklch(44, 0.11, 240.79);
        color_success = RGBColor.Oklch(84, 0.143, 164.978);
        color_success_content = RGBColor.Oklch(43, 0.095, 166.913);
        color_warning = RGBColor.Oklch(75, 0.183, 55.934);
        color_warning_content = RGBColor.Oklch(26, 0.079, 36.259);
        color_error = RGBColor.Oklch(63, 0.237, 25.331);
        color_error_content = RGBColor.Oklch(97, 0.013, 17.38);

        radius_box = 4;
        radius_field = 1;
        radius_selector = 1;

        size_field = 4;
        size_selector = 4;

        border = 1;
    }
}
