package legendev.sovereign.cardinal.component;

import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class RandComponent implements Component {

    private int value = (int) (Math.random() * 20);

    public int getValue() {
        return value;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.value = tag.getInt("value");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("value", this.value);
    }
}
