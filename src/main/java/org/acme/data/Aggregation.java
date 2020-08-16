package org.acme.data;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.time.Instant;

@Entity(name = "AVERAGES")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Aggregation extends PanacheEntityBase {

    @Id
    public String key;
    public int count;
    public Instant instant;

    public double capacityMin;
    public double capacityMax;
    public double capacitySum;
    public double capacityAvg;

    public double vibeMin;
    public double vibeMax;
    public double vibeSum;
    public double vibeAvg;

    @Entity(name = "AVERAGES_ROUTE_1")
    public static class AvgRoute1 extends Aggregation {
    }

    @Entity(name = "AVERAGES_ROUTE_5")
    public static class AvgRoute5 extends Aggregation {
    }

    @Entity(name = "AVERAGES_TRIP_1")
    public static class AvgTrip1 extends Aggregation {
    }

    @Entity(name = "AVERAGES_TRIP_5")
    public static class AvgTrip5 extends Aggregation {
    }

    public Aggregation() {
    }

    @Override
    public String toString() {
        return "Aggregation{" +
                "key='" + key + '\'' +
                ", count=" + count +
                ", instant=" + instant +
                ", capacityMin=" + capacityMin +
                ", capacityMax=" + capacityMax +
                ", capacitySum=" + capacitySum +
                ", capacityAvg=" + capacityAvg +
                ", vibeMin=" + vibeMin +
                ", vibeMax=" + vibeMax +
                ", vibeSum=" + vibeSum +
                ", vibeAvg=" + vibeAvg +
                '}';
    }
}
