package unnaincompris.LunaZ.utils;

import jline.internal.Nullable;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

public class ParticleUtils {
    public static void spawnParticleAlongLine(Location start, Location end, Particle particle, int pointsPerLine, int particleCount, double offsetX, double offsetY, double offsetZ, double extra, @Nullable Double data, boolean forceDisplay,
                                              @Nullable Predicate<Location> operationPerPoint) {
        double d = start.distance(end) / pointsPerLine;
        for (int i = 0; i < pointsPerLine; i++) {
            Location l = start.clone();
            Vector direction = end.toVector().subtract(start.toVector()).normalize();
            Vector v = direction.multiply(i * d);
            l.add(v.getX(), v.getY(), v.getZ());
            if (operationPerPoint == null) {
                start.getWorld().spawnParticle(particle, l, particleCount, offsetX, offsetY, offsetZ, extra, data, forceDisplay);
                continue;
            }
            if (operationPerPoint.test(l)) {
                start.getWorld().spawnParticle(particle, l, particleCount, offsetX, offsetY, offsetZ, extra, data, forceDisplay);
            }
        }
    }

    public static void spawnParticleAlongLine(Location start, Location end, Particle particle, int pointsPerLine, int particleCount, double extra, @Nullable Double data, boolean forceDisplay,
                                              @Nullable Predicate<Location> operationPerPoint) {
        spawnParticleAlongLine(start, end, particle, pointsPerLine, particleCount, 0, 0, 0, extra, data, forceDisplay, operationPerPoint);
    }
}
