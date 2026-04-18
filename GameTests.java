import action.*;
import effect.*;
import engine.*;
import item.*;
import model.*;
import ui.*;
import util.*;

import java.io.*;
import java.util.*;

public class GameTests {

    private static int passed = 0, failed = 0;

    private static void assertTrue(String name, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + name);
            passed++;
        } else {
            System.out.println("  FAIL: " + name);
            failed++;
        }
    }

    private static void assertEquals(String name, int expected, int actual) {
        assertTrue(name + " (expected=" + expected + ", actual=" + actual + ")", expected == actual);
    }

    private static void section(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    static void testDamageCalc() {
        section("1. Damage Calculation");

        Warrior w = new Warrior();
        Goblin g = new Goblin("Goblin");

        assertEquals("warrior vs goblin", 25, DamageCalculator.calculate(w, g));

        Wolf wolf = new Wolf("Wolf");
        assertEquals("wolf vs warrior", 25, DamageCalculator.calculate(wolf, w));

        assertEquals("goblin vs warrior", 15, DamageCalculator.calculate(g, w));

        assertEquals("warrior vs wolf", 35, DamageCalculator.calculate(w, wolf));

        Combatant tank = new Combatant("Tank", 100, 1, 999, 10) {};
        Combatant weakAttacker = new Goblin("Weak");
        assertEquals("damage cannot go negative", 0, DamageCalculator.calculate(weakAttacker, tank));

        Combatant a = new Combatant("A", 50, 20, 10, 10) {};
        Combatant b = new Combatant("B", 50, 10, 20, 10) {};
        assertEquals("atk equals def -> 0 damage", 0, DamageCalculator.calculate(a, b));
    }

    static void testHpClamping() {
        section("2. HP Clamping");

        Goblin g = new Goblin("G");
        assertEquals("starting HP", 55, g.getHp());

        g.takeDamage(9999);
        assertEquals("hp cannot go below 0", 0, g.getHp());
        assertTrue("isAlive false at 0 hp", !g.isAlive());

        Warrior w = new Warrior();
        w.takeDamage(100);
        assertEquals("hp after damage", 160, w.getHp());
        w.heal(9999);
        assertEquals("hp capped at maxHp", 260, w.getHp());

        Goblin dead = new Goblin("Dead");
        dead.takeDamage(9999);
        dead.heal(10);
        assertEquals("heal dead: hp is 10", 10, dead.getHp());
        assertTrue("isAlive after heal from 0", dead.isAlive());
    }

    static void testStunEffect() {
        section("3. Stun Effect");

        Goblin g = new Goblin("G");
        StunEffect stun = new StunEffect();
        g.getEffects().addEffect(stun);

        assertTrue("stun preventsAction immediately", g.getEffects().preventsAction());
        assertTrue("stun isNotExpired initially", !stun.isExpired());

        g.getEffects().tick();
        assertTrue("stun still active after 1 tick", g.getEffects().preventsAction());

        g.getEffects().tick();
        assertTrue("stun removed after 2 ticks", !g.getEffects().preventsAction());

        Goblin dead = new Goblin("Dead");
        dead.takeDamage(9999);
        dead.getEffects().addEffect(new StunEffect());
        assertTrue("stun on dead: preventsAction still true", dead.getEffects().preventsAction());
    }

    static void testDefendEffect() {
        section("4. Defend Effect");

        Warrior w = new Warrior();
        assertEquals("base defense", 20, w.getDefense());

        DefendEffect defend = new DefendEffect();
        w.getEffects().addEffect(defend);
        assertEquals("defense with buff", 30, w.getDefense());

        w.getEffects().tick();
        assertEquals("defense after 1 tick", 30, w.getDefense());

        w.getEffects().tick();
        assertEquals("defense after 2 ticks", 20, w.getDefense());

        Warrior w2 = new Warrior();
        w2.getEffects().addEffect(new DefendEffect());
        w2.getEffects().addEffect(new DefendEffect());
        assertEquals("two defend effects stack to +20", 40, w2.getDefense());
    }

    static void testSmokeBombEffect() {
        section("5. Smoke Bomb Invulnerability");

        Warrior w = new Warrior();
        SmokeBombInvulnerability smoke = new SmokeBombInvulnerability();
        w.getEffects().addEffect(smoke);

        assertTrue("invulnerable immediately", w.getEffects().isInvulnerable());

        int hpBefore = w.getHp();
        w.takeDamage(100);
        assertEquals("no damage while invulnerable", hpBefore, w.getHp());

        w.getEffects().tick();
        assertTrue("invulnerable after 1 tick", w.getEffects().isInvulnerable());
        w.takeDamage(100);
        assertEquals("still no damage after 1 tick", hpBefore, w.getHp());

        w.getEffects().tick();
        assertTrue("not invulnerable after 2 ticks", !w.getEffects().isInvulnerable());

        w.takeDamage(50);
        assertEquals("damage lands after smoke expired", hpBefore - 50, w.getHp());
    }

    static void testArcaneBlastBuff() {
        section("6. ArcaneBlast Buff (infinite duration)");

        Wizard wizard = new Wizard();
        assertEquals("wizard base atk", 50, wizard.getAttack());

        ArcaneBlastBuff buff = new ArcaneBlastBuff(10);
        wizard.getEffects().addEffect(buff);
        assertEquals("atk with 10 bonus", 60, wizard.getAttack());
        assertTrue("buff never expires", !buff.isExpired());

        for (int i = 0; i < 100; i++) {
            wizard.getEffects().tick();
        }
        assertEquals("atk still 60 after 100 ticks", 60, wizard.getAttack());
        assertTrue("buff still not expired after 100 ticks", !buff.isExpired());

        wizard.getEffects().addEffect(new ArcaneBlastBuff(10));
        assertEquals("two buffs stack to +20", 70, wizard.getAttack());
    }

    static void testArcaneBlastAction() {
        section("7. ArcaneBlast Action");

        Wizard wizard = new Wizard();
        wizard.getSkillCD().reset();

        Goblin g1 = new Goblin("G1");
        Goblin g2 = new Goblin("G2");

        ArcaneBlast blast = new ArcaneBlast(true);
        List<Combatant> targets = new ArrayList<>(List.of(g1, g2));
        blast.execute(wizard, targets);

        assertEquals("g1 hp after blast", 55 - 35, g1.getHp());
        assertEquals("g2 hp after blast", 55 - 35, g2.getHp());

        assertEquals("no buff if no kills", 50, wizard.getAttack());

        assertEquals("cooldown reset to 3", 3, wizard.getSkillCD().getRemaining());

        Wizard w2 = new Wizard();
        Goblin dying1 = new Goblin("D1");
        Goblin dying2 = new Goblin("D2");
        dying1.takeDamage(54);
        dying2.takeDamage(54);

        ArcaneBlast blast2 = new ArcaneBlast(true);
        blast2.execute(w2, new ArrayList<>(List.of(dying1, dying2)));

        assertTrue("D1 eliminated", !dying1.isAlive());
        assertTrue("D2 eliminated", !dying2.isAlive());
        assertEquals("atk after 2 kills", 70, w2.getAttack());
    }

    /**
     * Per the assignment appendix (Medium/Wizard Round 3):
     * Wolf A is hit with ATK 60 (55 dmg), killed -> ATK becomes 70.
     * Wolf B is then hit with ATK 70 (65 dmg).
     *
     * The current implementation applies the ATK buff AFTER all targets,
     * so Wolf B is hit with ATK 60 (55 dmg) instead of ATK 70 (65 dmg).
     * This test documents that the behavior DOES NOT match the spec.
     */
    static void testArcaneBlastMidIterationAtk() {
        section("8. ArcaneBlast Mid-Iteration ATK Update (spec compliance check)");

        Wizard wizard = new Wizard();
        wizard.getEffects().addEffect(new ArcaneBlastBuff(10));

        Wolf wolfA = new Wolf("Wolf A");
        Wolf wolfB = new Wolf("Wolf B");

        ArcaneBlast blast = new ArcaneBlast(false);
        blast.execute(wizard, new ArrayList<>(List.of(wolfA, wolfB)));

        assertTrue("Wolf A eliminated", !wolfA.isAlive());
        assertTrue("Wolf B eliminated", !wolfB.isAlive());

        assertEquals("final ATK after 2 kills", 80, wizard.getAttack());

        Wizard wizard2 = new Wizard();
        wizard2.getEffects().addEffect(new ArcaneBlastBuff(10));
        Wolf wolfA2 = new Wolf("WA2");
        wolfA2.takeDamage(39);
        Wolf wolfB2 = new Wolf("WB2");
        wolfB2.takeDamage(wolfB2.getMaxHp() - 60);

        Combatant toughTarget = new Combatant("Tough", 56, 10, 5, 10) {};

        Goblin killMe = new Goblin("KillMe");
        killMe.takeDamage(54);

        blast = new ArcaneBlast(false);
        blast.execute(wizard2, new ArrayList<>(List.of(killMe, toughTarget)));

        boolean specBehavior = !toughTarget.isAlive();
        boolean currentBehavior = toughTarget.getHp() == 1;

        if (currentBehavior) {
            System.out.println("  INFO: ArcaneBlast ATK update is AFTER all kills (current behavior).");
            System.out.println("        Spec shows ATK updating BETWEEN kills (see appendix Round 3 Medium/Wizard).");
            System.out.println("        Edge case: 56-HP target survives current code but should die per spec.");
        } else if (specBehavior) {
            System.out.println("  INFO: ArcaneBlast ATK update is mid-iteration (matches spec).");
        }
        assertTrue("arcane blast kills toughTarget per spec", specBehavior);
    }

    static void testShieldBashAction() {
        section("9. Shield Bash Action");

        Warrior warrior = new Warrior();
        Goblin goblin = new Goblin("G");

        ShieldBash bash = new ShieldBash(true);
        bash.execute(warrior, List.of(goblin));

        assertEquals("goblin hp after bash", 55 - 25, goblin.getHp());
        assertTrue("goblin is stunned", goblin.getEffects().preventsAction());
        assertEquals("warrior cooldown reset", 3, warrior.getSkillCD().getRemaining());

        Goblin dead = new Goblin("Dead");
        dead.takeDamage(9999);
        ShieldBash bash2 = new ShieldBash(false);
        bash2.execute(warrior, List.of(dead));
        assertTrue("bash on dead: no crash", true);
    }

    static void testCooldownSystem() {
        section("10. Cooldown System");

        Warrior warrior = new Warrior();
        assertTrue("skill ready initially", warrior.canUseSpecialSkillThisTurn());

        warrior.getSkillCD().reset();
        assertEquals("cooldown = 3 after reset", 3, warrior.getSkillCD().getRemaining());
        assertTrue("skill NOT ready", !warrior.canUseSpecialSkillThisTurn());

        warrior.getSkillCD().tick();
        assertEquals("cooldown = 2", 2, warrior.getSkillCD().getRemaining());

        warrior.getSkillCD().tick();
        assertEquals("cooldown = 1", 1, warrior.getSkillCD().getRemaining());

        warrior.getSkillCD().tick();
        assertEquals("cooldown = 0", 0, warrior.getSkillCD().getRemaining());
        assertTrue("skill ready again", warrior.canUseSpecialSkillThisTurn());

        warrior.getSkillCD().tick();
        assertEquals("cooldown stays 0 after extra tick", 0, warrior.getSkillCD().getRemaining());
    }

    static void testPotion() {
        section("11. Potion Item");

        Warrior warrior = new Warrior();
        Potion potion = new Potion();
        warrior.getInventory().addItem(potion);

        warrior.takeDamage(150);
        assertEquals("hp after damage", 110, warrior.getHp());

        potion.execute(warrior, List.of(warrior));
        assertEquals("hp after potion", 210, warrior.getHp());
        assertEquals("potion consumed", 0, warrior.getInventory().getItemCount());

        Warrior full = new Warrior();
        Potion p2 = new Potion();
        full.getInventory().addItem(p2);
        p2.execute(full, List.of(full));
        assertEquals("potion at full hp -> capped at maxHp", 260, full.getHp());

        Warrior dying = new Warrior();
        dying.takeDamage(259);
        Potion p3 = new Potion();
        dying.getInventory().addItem(p3);
        p3.execute(dying, List.of(dying));
        assertEquals("dying warrior healed: 1+100=101", 101, dying.getHp());
    }

    static void testSmokeBombItem() {
        section("12. Smoke Bomb Item");

        Warrior warrior = new Warrior();
        SmokeBomb smokeBomb = new SmokeBomb();
        warrior.getInventory().addItem(smokeBomb);

        smokeBomb.execute(warrior, List.of(warrior));
        assertTrue("warrior invulnerable after smoke bomb", warrior.getEffects().isInvulnerable());
        assertEquals("smoke bomb consumed", 0, warrior.getInventory().getItemCount());

        warrior.takeDamage(100);
        assertEquals("no damage while invulnerable", 260, warrior.getHp());
    }

    static void testPowerStone() {
        section("13. Power Stone Item");

        Warrior warrior = new Warrior();
        PowerStone ps = new PowerStone(warrior.createSpecialSkill(false));
        warrior.getInventory().addItem(ps);
        assertTrue("skill ready initially", warrior.canUseSpecialSkillThisTurn());

        Goblin target = new Goblin("G");
        ps.execute(warrior, List.of(target));

        assertEquals("goblin hp after PowerStone ShieldBash", 30, target.getHp());
        assertTrue("goblin stunned by PowerStone ShieldBash", target.getEffects().preventsAction());

        assertTrue("cooldown unchanged after PowerStone", warrior.canUseSpecialSkillThisTurn());

        assertEquals("power stone consumed", 0, warrior.getInventory().getItemCount());
    }

    static void testPowerStoneWizard() {
        section("14. Power Stone - Wizard");

        Wizard wizard = new Wizard();
        wizard.getSkillCD().reset();
        assertEquals("cooldown is 3", 3, wizard.getSkillCD().getRemaining());

        PowerStone ps = new PowerStone(wizard.createSpecialSkill(false));
        wizard.getInventory().addItem(ps);

        Goblin g1 = new Goblin("G1");
        g1.takeDamage(54);
        Goblin g2 = new Goblin("G2");

        ps.execute(wizard, new ArrayList<>(List.of(g1, g2)));

        assertTrue("G1 killed by PowerStone Arcane Blast", !g1.isAlive());
        assertEquals("G2 hp after PowerStone Arcane Blast", 10, g2.getHp());

        assertEquals("wizard atk after 1 kill", 60, wizard.getAttack());

        assertEquals("cooldown still 3 after PowerStone", 3, wizard.getSkillCD().getRemaining());

        assertEquals("power stone consumed", 0, wizard.getInventory().getItemCount());
    }

    static void testAvailableActions() {
        section("15. Available Actions");

        Warrior warrior = new Warrior();
        List<Action> actions = warrior.getAvailableActions();

        assertTrue("has BasicAttack", actions.stream().anyMatch(a -> a instanceof BasicAttack));
        assertTrue("has Defend", actions.stream().anyMatch(a -> a instanceof Defend));
        assertTrue("has ShieldBash", actions.stream().anyMatch(a -> a instanceof ShieldBash));
        assertEquals("3 actions when no items", 3, actions.size());

        Potion p = new Potion();
        warrior.getInventory().addItem(p);
        actions = warrior.getAvailableActions();
        assertEquals("4 actions with one item", 4, actions.size());

        warrior.getSkillCD().reset();
        actions = warrior.getAvailableActions();
        assertTrue("ShieldBash not in list when on cooldown",
            actions.stream().noneMatch(a -> a instanceof ShieldBash));
        assertEquals("3 actions when skill on cooldown and 1 item", 3, actions.size());
    }

    static void testTurnOrder() {
        section("16. Speed-Based Turn Order");

        Warrior warrior = new Warrior();
        Goblin goblin = new Goblin("G");
        Wolf wolf = new Wolf("W");

        strategy.SpeedBasedTurnOrder strategy = new strategy.SpeedBasedTurnOrder();

        List<Combatant> order = strategy.determineTurnOrder(List.of(warrior, goblin, wolf));
        assertEquals("first is fastest (wolf)", 35, order.get(0).getSpeed());
        assertEquals("second is warrior", 30, order.get(1).getSpeed());
        assertEquals("third is goblin", 25, order.get(2).getSpeed());

        goblin.takeDamage(9999);
        order = strategy.determineTurnOrder(List.of(warrior, goblin, wolf));
        assertEquals("only 2 alive in order", 2, order.size());
        assertTrue("dead goblin not in order", order.stream().noneMatch(c -> c == goblin));
    }

    static void testBattleEngineEasyMode() {
        section("17. Battle Engine - Easy Mode Simulation");

        Warrior warrior = new Warrior();
        Goblin goblinA = new Goblin("Goblin A");
        Goblin goblinB = new Goblin("Goblin B");
        Goblin goblinC = new Goblin("Goblin C");

        goblinA.takeDamage(DamageCalculator.calculate(warrior, goblinA));
        assertEquals("Goblin A HP after warrior attack (round 1)", 30, goblinA.getHp());

        warrior.takeDamage(DamageCalculator.calculate(goblinA, warrior));
        warrior.takeDamage(DamageCalculator.calculate(goblinB, warrior));
        warrior.takeDamage(DamageCalculator.calculate(goblinC, warrior));
        assertEquals("Warrior HP after 3 goblin attacks (round 1)", 215, warrior.getHp());

        ShieldBash bash = new ShieldBash(true);
        bash.execute(warrior, List.of(goblinA));
        assertEquals("Goblin A HP after Shield Bash (round 2)", 5, goblinA.getHp());
        assertTrue("Goblin A stunned", goblinA.getEffects().preventsAction());
        assertEquals("Warrior cooldown set to 3", 3, warrior.getSkillCD().getRemaining());

        assertTrue("Goblin A cannot act (stunned)", goblinA.getEffects().preventsAction());

        warrior.takeDamage(DamageCalculator.calculate(goblinB, warrior));
        warrior.takeDamage(DamageCalculator.calculate(goblinC, warrior));
        assertEquals("Warrior HP end of round 2", 185, warrior.getHp());

        warrior.getEffects().tick();
        goblinA.getEffects().tick();
        goblinB.getEffects().tick();
        goblinC.getEffects().tick();
        warrior.getSkillCD().tick();
        assertEquals("Warrior cooldown after round 2 tick", 2, warrior.getSkillCD().getRemaining());
        assertTrue("Goblin A still stunned after round 2 tick", goblinA.getEffects().preventsAction());

        warrior.takeDamage(0);
        goblinA.takeDamage(DamageCalculator.calculate(warrior, goblinA));
        assertEquals("Goblin A HP after round 3 attack", 0, goblinA.getHp());
        assertTrue("Goblin A eliminated", !goblinA.isAlive());

        assertTrue("Goblin A is dead (eliminated check)", !goblinA.isAlive());

        warrior.takeDamage(DamageCalculator.calculate(goblinB, warrior));
        warrior.takeDamage(DamageCalculator.calculate(goblinC, warrior));
        assertEquals("Warrior HP end of round 3", 155, warrior.getHp());

        warrior.getEffects().tick();
        goblinA.getEffects().tick();
        goblinB.getEffects().tick();
        goblinC.getEffects().tick();
        warrior.getSkillCD().tick();
        assertEquals("Warrior cooldown after round 3 tick", 1, warrior.getSkillCD().getRemaining());

        System.out.println("  PASS: Easy mode rounds 1-3 match appendix values");
    }

    static void testBackupSpawn() {
        section("18. Backup Spawn Logic");

        GameUI fakeUI = createFakeUI(new int[]{
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        });

        Warrior warrior = new Warrior();
        BattleEngine engine = new BattleEngine(warrior, Level.MEDIUM, fakeUI);

        List<Enemy> initial = engine.getAliveEnemies();
        assertEquals("MEDIUM: 2 initial enemies", 2, initial.size());

        BattleEngine.BattleResult result = engine.startBattle();
        assertTrue("battle completes without crash", result != null);
    }

    static void testLevelEnemyPools() {
        section("19. Level Enemy Pools");

        List<Enemy> easyInitial = Level.EASY.createInitialEnemies();
        assertEquals("EASY: 3 initial enemies", 3, easyInitial.size());
        assertTrue("EASY: all goblins", easyInitial.stream().allMatch(e -> e instanceof Goblin));
        assertTrue("EASY: no backup", !Level.EASY.hasBackupSpawn());

        List<Enemy> medInitial = Level.MEDIUM.createInitialEnemies();
        assertEquals("MEDIUM: 2 initial enemies", 2, medInitial.size());
        List<Enemy> medBackup = Level.MEDIUM.createBackupEnemies();
        assertEquals("MEDIUM: 2 backup wolves", 2, medBackup.size());
        assertTrue("MEDIUM: backup are wolves", medBackup.stream().allMatch(e -> e instanceof Wolf));
        assertTrue("MEDIUM: has backup", Level.MEDIUM.hasBackupSpawn());

        List<Enemy> hardInitial = Level.HARD.createInitialEnemies();
        assertEquals("HARD: 2 initial goblins", 2, hardInitial.size());
        List<Enemy> hardBackup = Level.HARD.createBackupEnemies();
        assertEquals("HARD: 3 backup enemies", 3, hardBackup.size());
        assertTrue("HARD: has backup", Level.HARD.hasBackupSpawn());
    }

    static void testEnemyNaming() {
        section("20. Enemy Naming Convention");

        List<Enemy> easyEnemies = Level.EASY.createInitialEnemies();
        List<String> easyNames = easyEnemies.stream().map(Enemy::getName).toList();
        assertTrue("Goblin A exists", easyNames.contains("Goblin A"));
        assertTrue("Goblin B exists", easyNames.contains("Goblin B"));
        assertTrue("Goblin C exists", easyNames.contains("Goblin C"));

        List<Enemy> medEnemies = Level.MEDIUM.createInitialEnemies();
        List<String> medNames = medEnemies.stream().map(Enemy::getName).toList();
        assertTrue("MEDIUM: single Goblin named 'Goblin'", medNames.contains("Goblin"));
        assertTrue("MEDIUM: single Wolf named 'Wolf'", medNames.contains("Wolf"));
    }

    static void testDefendAction() {
        section("21. Defend Action");

        Warrior warrior = new Warrior();
        assertEquals("base DEF", 20, warrior.getDefense());

        Defend defend = new Defend();
        defend.execute(warrior, List.of(warrior));
        assertEquals("DEF after defend", 30, warrior.getDefense());

        Goblin goblin = new Goblin("G");
        int damage = DamageCalculator.calculate(goblin, warrior);
        assertEquals("damage reduced by defend", 5, damage);

        warrior.getEffects().tick();
        warrior.getEffects().tick();
        assertEquals("DEF back to base after 2 ticks", 20, warrior.getDefense());
    }

    static void testStatusEffectManagerMultiple() {
        section("22. StatusEffectManager - Multiple Effects");

        Warrior warrior = new Warrior();

        warrior.getEffects().addEffect(new StunEffect());
        warrior.getEffects().addEffect(new DefendEffect());

        assertTrue("stunned", warrior.getEffects().preventsAction());
        assertEquals("defend bonus active", 30, warrior.getDefense());

        warrior.getEffects().tick();
        warrior.getEffects().tick();
        assertTrue("not stunned after 2 ticks", !warrior.getEffects().preventsAction());
        assertEquals("defend gone after 2 ticks", 20, warrior.getDefense());

        Warrior w2 = new Warrior();
        w2.getEffects().addEffect(new SmokeBombInvulnerability());
        w2.getEffects().addEffect(new StunEffect());
        assertTrue("invulnerable and stunned", w2.getEffects().isInvulnerable() && w2.getEffects().preventsAction());
    }

    static void testCombatantPolymorphism() {
        section("23. Player/Enemy interchangeable as Combatant (LSP)");

        List<Combatant> combatants = new ArrayList<>();
        combatants.add(new Warrior());
        combatants.add(new Wizard());
        combatants.add(new Goblin("G"));
        combatants.add(new Wolf("W"));

        for (Combatant c : combatants) {
            assertTrue(c.getName() + " isAlive", c.isAlive());
            assertTrue(c.getName() + " getHp > 0", c.getHp() > 0);
            c.takeDamage(9999);
            assertTrue(c.getName() + " not alive after 9999 dmg", !c.isAlive());
        }
    }

    static void testBasicAttackAction() {
        section("24. Basic Attack Action");

        Warrior warrior = new Warrior();
        Goblin goblin = new Goblin("G");

        BasicAttack attack = new BasicAttack();
        assertTrue("target mode single", TargetMode.SINGLE_OPPONENT == attack.getTargetMode(warrior));
        attack.execute(warrior, List.of(goblin));
        assertEquals("goblin hp after basic attack", 30, goblin.getHp());

        Goblin invul = new Goblin("Invul");
        invul.getEffects().addEffect(new SmokeBombInvulnerability());
        attack.execute(warrior, List.of(invul));
        assertEquals("invulnerable goblin takes 0 dmg", 55, invul.getHp());
    }

    static void testGameConfiguration() {
        section("25. GameConfiguration");

        List<StartingItemType> items = List.of(StartingItemType.POTION, StartingItemType.SMOKE_BOMB);
        GameConfiguration config = new GameConfiguration(PlayerClassOption.WARRIOR, items, Level.EASY);

        Player player = config.createPlayer();
        assertTrue("player is warrior", player instanceof Warrior);
        assertEquals("player has 2 items", 2, player.getInventory().getItemCount());

        try {
            new GameConfiguration(null, items, Level.EASY);
            assertTrue("null playerClass should throw", false);
        } catch (IllegalArgumentException e) {
            assertTrue("null playerClass throws IAE", true);
        }

        try {
            new GameConfiguration(PlayerClassOption.WARRIOR, null, Level.EASY);
            assertTrue("null items should throw", false);
        } catch (IllegalArgumentException e) {
            assertTrue("null items throws IAE", true);
        }

        try {
            new GameConfiguration(PlayerClassOption.WARRIOR, List.of(StartingItemType.POTION), Level.EASY);
            assertTrue("wrong items count should throw", false);
        } catch (IllegalArgumentException e) {
            assertTrue("wrong items count throws IAE", true);
        }
    }

    static void testWizardMediumRound1() {
        section("26. Wizard Medium Round 1 (Appendix)");

        Wizard wizard = new Wizard();
        Wolf wolf = new Wolf("Wolf");
        Goblin goblin = new Goblin("Goblin");

        wizard.takeDamage(DamageCalculator.calculate(wolf, wizard));
        assertEquals("wizard hp after wolf attack", 165, wizard.getHp());

        wizard.takeDamage(DamageCalculator.calculate(goblin, wizard));
        assertEquals("wizard hp after goblin attack", 140, wizard.getHp());

        ArcaneBlast blast = new ArcaneBlast(true);
        blast.execute(wizard, new ArrayList<>(List.of(goblin, wolf)));

        assertEquals("goblin hp after blast", 20, goblin.getHp());
        assertTrue("goblin survives", goblin.isAlive());

        assertEquals("wolf hp after blast", 0, wolf.getHp());
        assertTrue("wolf eliminated", !wolf.isAlive());

        assertEquals("wizard atk after 1 kill", 60, wizard.getAttack());
        assertEquals("wizard cooldown reset", 3, wizard.getSkillCD().getRemaining());
    }

    static void testZeroSpeedCombatant() {
        section("27. Edge: Zero-Speed Combatant");

        Combatant slow = new Combatant("Slow", 100, 10, 10, 0) {};
        Goblin normal = new Goblin("G");
        strategy.SpeedBasedTurnOrder strat = new strategy.SpeedBasedTurnOrder();
        List<Combatant> order = strat.determineTurnOrder(List.of(slow, normal));
        assertEquals("normal before slow", 25, order.get(0).getSpeed());
        assertEquals("slow last", 0, order.get(1).getSpeed());
    }

    static void testDuplicateItems() {
        section("28. Edge: Duplicate Items in Inventory");

        Warrior warrior = new Warrior();
        Potion p1 = new Potion();
        Potion p2 = new Potion();
        warrior.getInventory().addItem(p1);
        warrior.getInventory().addItem(p2);
        assertEquals("2 potions in inventory", 2, warrior.getInventory().getItemCount());

        warrior.takeDamage(150);
        p1.execute(warrior, List.of(warrior));
        assertEquals("1 potion left after use", 1, warrior.getInventory().getItemCount());

        p2.execute(warrior, List.of(warrior));
        assertEquals("0 potions left", 0, warrior.getInventory().getItemCount());
    }

    static void testArcaneBlastEmptyTargets() {
        section("29. Edge: ArcaneBlast on Empty Target List");

        Wizard wizard = new Wizard();
        ArcaneBlast blast = new ArcaneBlast(true);

        blast.execute(wizard, new ArrayList<>());
        assertEquals("no atk change with no targets", 50, wizard.getAttack());
        assertEquals("cooldown reset even with no targets", 3, wizard.getSkillCD().getRemaining());
    }

    static void testStunAndElimination() {
        section("30. Edge: Stun expiry coincides with elimination");

        Goblin g = new Goblin("G");
        g.getEffects().addEffect(new StunEffect());

        g.getEffects().tick();
        g.getEffects().tick();
        assertTrue("stun expired", !g.getEffects().preventsAction());

        g.takeDamage(9999);
        assertTrue("goblin dead", !g.isAlive());
        g.getEffects().tick();
        assertTrue("no crash on dead goblin tick", true);
    }

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("  SC2002 Turn-Based Combat Test Suite");
        System.out.println("====================================");

        testDamageCalc();
        testHpClamping();
        testStunEffect();
        testDefendEffect();
        testSmokeBombEffect();
        testArcaneBlastBuff();
        testArcaneBlastAction();
        testArcaneBlastMidIterationAtk();
        testShieldBashAction();
        testCooldownSystem();
        testPotion();
        testSmokeBombItem();
        testPowerStone();
        testPowerStoneWizard();
        testAvailableActions();
        testTurnOrder();
        testBattleEngineEasyMode();
        testBackupSpawn();
        testLevelEnemyPools();
        testEnemyNaming();
        testDefendAction();
        testStatusEffectManagerMultiple();
        testCombatantPolymorphism();
        testBasicAttackAction();
        testGameConfiguration();
        testWizardMediumRound1();
        testZeroSpeedCombatant();
        testDuplicateItems();
        testArcaneBlastEmptyTargets();
        testStunAndElimination();

        System.out.println("\n====================================");
        System.out.printf("  Results: %d passed, %d failed%n", passed, failed);
        System.out.println("====================================");
    }

    private static GameUI createFakeUI(int[] inputSequence) {
        return new GameUI() {
            int idx = 0;

            private int next() {
                if (idx < inputSequence.length) return inputSequence[idx++];
                return 0;
            }

            @Override public void displayLoadingScreen() {}
            @Override public int promptActionSelection(List<Action> actions) { return next(); }
            @Override public int promptTargetSelection(List<? extends Combatant> targets) { return next(); }
            @Override public void displayRoundInfo(int r, List<Combatant> t) {}
            @Override public void displayTurnInfo(Combatant c, Action a, List<Combatant> t) {}
            @Override public void displaySkippedTurn(Combatant c) {}
            @Override public void displayBackupSpawn(List<Enemy> e) {}
            @Override public void displayBattleState(Player p, List<Enemy> e) {}
            @Override public void displayVictory(Player p, int r) {}
            @Override public void displayDefeat(List<Enemy> e, int r) {}
        };
    }
}
