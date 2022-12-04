package unnaincompris.LunaZ.Manager.Weapon.Bullet;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerStatus;
import unnaincompris.LunaZ.Manager.Weapon.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BulletShooter {
    public BulletShooter(int speed) {
        this.speed = speed;
    }

    public final int speed;
    private int liveTick = 0;

    private Player shooter;
    private Weapon weapon;

    private Location currentLocation;
    private Location oldPos;
    private Vector bulletVector;
    public void shoot(Weapon weapon, Player shooter) {
        this.weapon = weapon;
        this.shooter = shooter;
        double pitchRad = ((shooter.getLocation().getPitch() + 90) * Math.PI) / 180;
        double yawRad  = ((shooter.getLocation().getYaw() + 90)  * Math.PI) / 180;
        Random random = new Random();
        double dispersion = weapon.dispersion;
        PlayerStatus playerStatus = Main.getInstance().getPlayerDataManager().getDataFromUUID(shooter.getUniqueId()).getPlayerStatus();
        if(shooter.isSneaking())
            dispersion *= 0.75;
        if(shooter.isSprinting())
            dispersion *= 1.5;
        if(playerStatus.zooming)
            dispersion *= 0.5;
        bulletVector = new Vector(
                Math.sin(pitchRad) * Math.cos(yawRad) /* x */ + random.nextDouble(-dispersion / 20, dispersion / 20),
                Math.cos(pitchRad)                    /* z */ + random.nextDouble(-dispersion / 20, dispersion / 20),
                Math.sin(pitchRad) * Math.sin(yawRad) /* y */ + random.nextDouble(-dispersion / 20, dispersion / 20));
        bulletVector.multiply(speed);
        currentLocation = shooter.getEyeLocation();
        oldPos = currentLocation.clone();
        new BukkitRunnable() {
            @Override
            public void run() {
                changeBulletVector();
                spawnTrail();

                if(checkCollision() || liveTick > 300) {
                    cancel();
                    return;
                }
                oldPos = currentLocation.clone();
                liveTick++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }

    private void changeBulletVector() {
        bulletVector.multiply(0.99);
        if(currentLocation.getBlock().isLiquid())
            bulletVector.multiply(0.25);
        else
            applyGravity();
        currentLocation = currentLocation.add(bulletVector);
    }
    private final int bulletWeight = (12 + 31 + 114) / 3;
    private void applyGravity() {
        bulletVector.add(new Vector(0, 0, -(bulletWeight / 1000f /* to kg */ * 9.81 /* gravity */) - bulletWeight / 1000f));
    }

    private Particle.DustOptions trailOption = new Particle.DustOptions(Color.fromRGB(255, 128, 0), 0.25F);
    private void spawnTrail() {
        Color color = trailOption.getColor();
        if(color.getRed() > 128) {
            trailOption = new Particle.DustOptions(Color.fromRGB(color.getRed() - 8, 128, color.getBlue() + 8), 0.25F);
        }
        World world = currentLocation.getWorld();
        double distance = oldPos.distance(currentLocation);
        Location cOldPos = oldPos.clone();
        Vector vCurrentLoc = currentLocation.toVector();
        Vector vector = vCurrentLoc.clone().subtract(cOldPos.toVector()).normalize().multiply(0.2);

        for (double length = 0 ; length < Math.abs(distance); cOldPos.add(vector)) {
            if(isCollide(oldPos.getBlock())) break;
            world.spawnParticle(Particle.REDSTONE, cOldPos, 1, trailOption);
            length += 0.4;
        }
    }

    public boolean checkCollision() {
        double blockTravel = oldPos.distance(currentLocation);
        Location location = oldPos.clone();
        Vector vector = currentLocation.toVector().clone().subtract(location.toVector()).normalize();
        for (int length = 0 ; length < Math.abs(blockTravel) ; length++) {
            location.add(vector);
            if(isCollide(location.getBlock()))
                return true;
            List<Entity> nearEntity = new ArrayList<>(location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5));
            if(!nearEntity.isEmpty()) {
                if(nearEntity.get(0) instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) nearEntity.get(0);
                    if(entity.equals(shooter)) return false;
                    weapon.affectDamage(entity, shooter);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCollide(Block block) {
        return !(block.isEmpty() || block.isLiquid() || block.isPassable());
    }
}
