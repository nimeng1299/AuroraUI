package neuvillette.AuroraUI.api.color;

public record RGBColor(int red, int green, int blue) {

    public int toRGB(){
        return (red << 16) + (green << 8) + blue;
    }

    /**
     * RGB → RGBA（alpha = 255）
     */
    public int toARGB() {
        return toARGB(255);
    }


    public int toARGB(int alpha) {
        return (alpha << 24) | (red << 16) + (green << 8) + blue;
    }

    public int toRGBA(int alpha){
        return (red << 24) | (green << 16) | (blue << 8) | alpha;
    }

    public RGBAColor toRGBAColor(int alpha){
        return new RGBAColor(red, green, blue, alpha);
    }

    /**
     * OKLCH → RGB（sRGB 0-255）
     *
     * @param l        亮度(可用百分比)
     * @param c        色度
     * @param h        色相角度
     */
    public static RGBColor fromOklch(double l, double c, double h){
        int[] rgb = Oklch.convert(l, c, h);
        return new RGBColor(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * OKLCH → RGB（sRGB 0-255）
     *
     * @param l        亮度(可用百分比)
     * @param c        色度
     * @param h        色相角度
     */
    public static RGBColor Oklch(double l, double c, double h){
        return fromOklch(l, c, h);
    }

    public static class Oklch {

        /**
         * OKLCH → RGB（sRGB 0-255）
         *
         * @param l        亮度(可用百分比)
         * @param c        色度
         * @param h        色相角度
         */
        public static int[] convert(double l, double c, double h) {
            double L = l;
            if (l > 1) {
                L = l / 100.0;
            }
            L = Math.clamp(L, 0.0, 1.0);

            // 色相保持裁剪
            double low = 0.0, high = 0.5, maxSafeC = 0.0;
            for (int i = 0; i < 30; i++) {
                double mid = (low + high) / 2.0;
                double[] lib = oklchToOklab(L, mid, h);
                double[] lin = oklabToLinearRgb(lib[0], lib[1], lib[2]);
                if (isInGamut(lin)) {
                    maxSafeC = mid;
                    low = mid;
                } else {
                    high = mid;
                }
            }

            double finalC = Math.min(c, maxSafeC);
            double[] lib = oklchToOklab(L, finalC, h);
            double[] lin = oklabToLinearRgb(lib[0], lib[1], lib[2]);

            return new int[]{
                    gammaCorrect(lin[0]),
                    gammaCorrect(lin[1]),
                    gammaCorrect(lin[2])
            };
        }


        private static double[] oklchToOklab(double L, double C, double H) {
            double rad = Math.toRadians(H);
            return new double[]{L, C * Math.cos(rad), C * Math.sin(rad)};
        }

        private static double[] oklabToLinearRgb(double L, double a, double b) {
            double lp = L + 0.3963377774 * a + 0.2158037573 * b;
            double mp = L - 0.1055613458 * a - 0.0638541728 * b;
            double sp = L - 0.0894841775 * a - 1.2914855480 * b;
            double l = lp * lp * lp, m = mp * mp * mp, s = sp * sp * sp;

            return new double[]{
                    4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s,
                    -1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s,
                    -0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s
            };
        }

        private static int gammaCorrect(double c) {
            c = (c <= 0.0031308) ? c * 12.92 : 1.055 * Math.pow(c, 1.0 / 2.4) - 0.055;
            return (int) Math.round(Math.clamp(c, 0.0, 1.0) * 255.0);
        }

        private static boolean isInGamut(double[] rgb) {
            for (double v : rgb) if (v < -1e-9 || v > 1.0 + 1e-9) return false;
            return true;
        }
    }
}
