package net.vitacraft.discordbotmanager.sandbox;

import net.vitacraft.discordbotmanager.Common;
import net.vitacraft.discordbotmanager.constants.MemoryUnit;

import java.util.Map;

public class ResourceManager {
    private final Common common;

    public ResourceManager(Common common){
        this.common = common;
    }

    public boolean canFitMemoryQuota(Integer mem){
        return getAllocatedMem() + mem <= getMaxMem(MemoryUnit.MEGABYTES);
    }

    public long getMaxMem(MemoryUnit unit) {
        return Runtime.getRuntime().maxMemory() / unit.getMultiplier();
    }

    public long getAllocatedMem() {
        Map<String, Sandbox> sandboxes = this.common.getSandboxManager().getAllSandboxes();
        Integer allocatedRam = 0;
        for (Sandbox sandbox : sandboxes.values()){
            allocatedRam = allocatedRam + sandbox.getSettings().ram();
        }
        return allocatedRam;
    }

    public long getUsedMem(MemoryUnit unit) {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / unit.getMultiplier();
    }

    public long getFreeMem(MemoryUnit unit) {
        return getMaxMem(unit) - getUsedMem(unit);
    }
}
