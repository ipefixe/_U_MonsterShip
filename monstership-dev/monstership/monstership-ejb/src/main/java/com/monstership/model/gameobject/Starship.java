package com.monstership.model.gameobject;

import com.monstership.model.Enhancements;
import com.monstership.model.Member;
import com.monstership.model.Upgradable;
import com.monstership.model.log.CombatLog;
import com.monstership.model.module.Module;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Starship extends GameObject implements Upgradable {
    private long monsterCount;
    private long actionPoint = DEFAULT_ACTION_POINT;
    private int level = 1;

    @ManyToOne(optional = false)
    private Member member;

    @OneToMany(targetEntity = CombatLog.class, mappedBy = "starship", fetch = FetchType.LAZY)
    private Set<CombatLog> combatLogs;

    @OneToMany(targetEntity = Module.class, mappedBy = "starship")
    private Set<Module> modules;

    public Starship() {
        this.setModel("starship_carcass");
    }

    public long getMonsterCount() {
        return monsterCount;
    }

    public synchronized void setMonsterCount(long monsterCount) {
        this.monsterCount = monsterCount;
    }

    public long getActionPoint() {
        return actionPoint;
    }

    public void setActionPoint(long actionPoint) {
        this.actionPoint = actionPoint;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public synchronized boolean upgrade() {
        long upgradeCost = upgradeCost();
        if (getMonsterCount() >= upgradeCost) {
            level += 1;
            setMonsterCount(getMonsterCount() - upgradeCost);
            return true;
        }
        return false;
    }

    @Override
    public long upgradeCost() {
        return 2 * level * level + 50 * level + 20;
    }

    public Enhancements getTotalEnhancements() {
        Enhancements ret = new Enhancements();
        for (Module module : this.modules) {
            Enhancements effect = module.effect();
            if (effect != null) {
                ret.setDodge(ret.getDodge() + effect.getDodge());
                ret.setPower(ret.getPower() + effect.getPower());
                ret.setSpeed(ret.getSpeed() + effect.getSpeed());
            }
        }
        return ret;
    }
}
