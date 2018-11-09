package net.lapidist.colony.shaders;

import com.badlogic.gdx.graphics.g3d.Attribute;

public class Attributes {

    public static class SunAttribute extends Attribute {

        public final static String Alias = "sun";
        public final static long ID = register(Alias);

        public static SunAttribute createSun() {
            return new SunAttribute(ID);
        }

        public String value;

        public SunAttribute (long type) {
            super(type);
        }

        public SunAttribute (final String value) {
            super(ID);
            this.value = value;
        }

        @Override
        public Attribute copy() {
            return new SunAttribute(value);
        }

        @Override
        protected boolean equals (Attribute other) {
            return ((SunAttribute) other).value.equals(value);
        }

        @Override
        public int hashCode () {
            int result = super.hashCode();
            result = 953 * result + value.hashCode();
            return result;
        }

        @Override
        public int compareTo(Attribute o) {
            if (type != o.type) return type < o.type ? -1 : 1;
            String otherValue = ((SunAttribute)o).value;
            return (value.equals(otherValue)) ? 0 : 1;
        }
    }
}
