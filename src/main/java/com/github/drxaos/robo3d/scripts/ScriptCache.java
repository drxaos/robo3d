package com.github.drxaos.robo3d.scripts;

import org.dynjs.runtime.JSProgram;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScriptCache {

    class Entry {
        JSProgram jsProgram;
        long access;

        public Entry(JSProgram jsProgram) {
            this.jsProgram = jsProgram;
            access = System.currentTimeMillis();
        }
    }

    Map<String, Entry> cache = Collections.synchronizedMap(new HashMap<String, Entry>());

    public void put(String name, JSProgram jsProgram) {
        cache.put(name, new Entry(jsProgram));
        for (Iterator<Entry> it = cache.values().iterator(); it.hasNext(); ) {
            Entry e = it.next();
            if (e.access < System.currentTimeMillis() - 10000) {
                it.remove();
            }
        }
    }

    public JSProgram get(String name) {
        Entry entry = cache.get(name);
        if (entry == null) {
            return null;
        }
        entry.access = System.currentTimeMillis();
        return entry.jsProgram;
    }

}
