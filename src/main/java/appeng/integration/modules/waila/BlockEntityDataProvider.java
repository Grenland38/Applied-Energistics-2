/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.integration.modules.waila;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;

import appeng.integration.modules.waila.tile.ChargerDataProvider;
import appeng.integration.modules.waila.tile.CraftingMonitorDataProvider;
import appeng.integration.modules.waila.tile.GridNodeStateDataProvider;
import appeng.integration.modules.waila.tile.PowerStorageDataProvider;

/**
 * Delegation provider for tiles through {@link mcp.mobius.waila.api.IComponentProvider}
 *
 * @author thatsIch
 * @version rv2
 * @since rv2
 */
public final class BlockEntityDataProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {
    /**
     * Contains all providers
     */
    private final List<BaseDataProvider> providers;

    /**
     * Initializes the provider list with all wanted providers
     */
    public BlockEntityDataProvider() {
        this.providers = List.of(
                new ChargerDataProvider(),
                new PowerStorageDataProvider(),
                new GridNodeStateDataProvider(),
                new CraftingMonitorDataProvider());
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        // Removes the built-in Forge-Energy progressbar
        tooltip.remove(new ResourceLocation("minecraft:fe"));

        for (var provider : providers) {
            provider.appendTooltip(tooltip, accessor, config);
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, ServerPlayer player, Level level, BlockEntity blockEntity,
            boolean showDetails) {
        for (var provider : providers) {
            provider.appendServerData(tag, player, level, blockEntity, showDetails);
        }
    }

}
