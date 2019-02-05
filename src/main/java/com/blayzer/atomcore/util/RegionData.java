package com.blayzer.atomcore.util;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

public class RegionData implements Serializable
{
    public String name;
    public Area area;
    
    public RegionData(String name, Area area) {
        this.name = name;
        this.area = area;
    }

    @Override
    public String toString() {
        return "[" + this.name + ", " + this.area + "]";
    }
    
    public static class Area implements Serializable
    {
        public Vec3 min;
        public Vec3 max;
        public String world;
        
        public Area(final Vec3 min, final Vec3 max, final String world) {
            this.min = min;
            this.max = max;
            this.min.x = Math.min(min.x, max.x);
            this.min.y = Math.min(min.y, max.y);
            this.min.z = Math.min(min.z, max.z);
            this.max.x = Math.max(min.x, max.x);
            this.max.y = Math.max(min.y, max.y);
            this.max.z = Math.max(min.z, max.z);
            this.world = world;
        }
        
        @Override
        public String toString() {
            return "{" + this.min + ", " + this.max + "}";
        }
        
        public boolean has(final int x, final int z) {
            return this.max.x >= x && this.min.x <= x && this.max.z >= z && this.min.z <= z;
        }
        
        public boolean has(final int x, final int y, final int z) {
            return this.has(x, z) && this.max.y >= y && this.min.y <= y;
        }
        
        public Area clone() {
            return new Area(this.min, this.max, this.world);
        }
        
        public static class Vec3 implements Serializable
        {
            public int x;
            public int y;
            public int z;
            
            public Vec3(final int x, final int y, final int z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }
            
            @Override
            public String toString() {
                return "[" + this.x + ", " + this.y + ", " + this.z + "]";
            }
        }
    }
}
