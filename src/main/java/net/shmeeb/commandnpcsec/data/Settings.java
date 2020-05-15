package net.shmeeb.commandnpcsec.data;

import java.util.Optional;
import javax.annotation.Generated;

import net.shmeeb.commandnpcsec.Main;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-02-05T09:28:06.815Z")
public class Settings extends AbstractData<Settings, Settings.Immutable> {
    private String cmd;

    {
        registerGettersAndSetters();
    }

    Settings() {
        cmd = "";
    }

    public Settings(String cmd) {
        this.cmd = cmd;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(Main.CMD, this::getCmd);
        registerFieldSetter(Main.CMD, this::setCmd);
        registerKeyValue(Main.CMD, this::cmd);
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Value<String> cmd() {
        return Sponge.getRegistry().getValueFactory().createValue(Main.CMD, cmd);
    }

    @Override
    public Optional<Settings> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(Settings.class).ifPresent(that -> {
            Settings data = overlap.merge(this, that);
            this.cmd = data.cmd;
        });
        return Optional.of(this);
    }

    @Override
    public Optional<Settings> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<Settings> from(DataView container) {
        container.getString(Main.CMD.getQuery()).ifPresent(v -> cmd = v);
        return Optional.of(this);
    }

    @Override
    public Settings copy() {
        return new Settings(cmd);
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(cmd);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(Main.CMD.getQuery(), cmd);
    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-02-05T09:28:06.827Z")
    public static class Immutable extends AbstractImmutableData<Immutable, Settings> {

        private String cmd;
        {
            registerGetters();
        }

        Immutable() {
            cmd = "";
        }

        Immutable(String cmd) {
            this.cmd = cmd;
        }

        @Override
        protected void registerGetters() {
            registerFieldGetter(Main.CMD, this::getCmd);
            registerKeyValue(Main.CMD, this::cmd);
        }

        public String getCmd() {
            return cmd;
        }

        public ImmutableValue<String> cmd() {
            return Sponge.getRegistry().getValueFactory().createValue(Main.CMD, cmd).asImmutable();
        }

        @Override
        public Settings asMutable() {
            return new Settings(cmd);
        }

        @Override
        public int getContentVersion() {
            return 1;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer()
                    .set(Main.CMD.getQuery(), cmd);
        }

    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-02-05T09:28:06.829Z")
    public static class Builder extends AbstractDataBuilder<Settings> implements DataManipulatorBuilder<Settings, Immutable> {

        public Builder() {
            super(Settings.class, 1);
        }

        @Override
        public Settings create() {
            return new Settings();
        }

        @Override
        public Optional<Settings> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<Settings> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }

    }
}