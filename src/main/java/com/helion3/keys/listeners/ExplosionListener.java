/**
 * This file is part of Keys, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 Helion3 http://helion3.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.helion3.keys.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.helion3.keys.Keys;

public class ExplosionListener {
    @Listener
    public void onExplosion(final ExplosionEvent.Detonate event) {
        Optional<Entity> optional = event.getCause().first(Entity.class);
        if (optional.isPresent()) {
            List<Location<World>> protectedLocations = new ArrayList<>();
            
            for (Location<World> location : event.getAffectedLocations()) {  
                if (Keys.getLockableBlocks().contains(location.getBlock().getType())) {
                    try {
                        // Are there locks on this block?
                        if (!Keys.getStorageAdapter().getLocks(location).isEmpty()) {
                            protectedLocations.add(location);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            for (Location<World> location : protectedLocations)
                event.getAffectedLocations().remove(location);
        }
    }
}