package com.rpissarra.car;

import java.math.BigDecimal;
import java.util.UUID;

public class CarArrayDataAccessService implements CarDao {

    private static final Car[] cars;

    static {
        cars = new Car[]{
                new Car(UUID.fromString("2ea85178-fada-4279-9d5e-eea627049fa2"), "A1", new BigDecimal("6.0"), Brand.AUDI, false),
                new Car(UUID.fromString("576590ff-57a1-4df3-8430-79980eb42343"), "TE1", new BigDecimal("7.0"), Brand.TESLA, true),
                new Car(UUID.fromString("9d818235-ce3b-40e8-b74a-3674985c6bcd"), "TO1", new BigDecimal("5.0"), Brand.TOYOTA, false),
                new Car(UUID.fromString("87cb62d9-d262-4174-b1b2-957f9e2a1f40"), "M1", new BigDecimal("7.0"), Brand.MERCEDES, false),
                new Car(UUID.fromString("10b474a9-f8c8-4976-8258-fcfd328dd491"), "TE2", new BigDecimal("9.0"), Brand.TESLA, true),
                new Car(UUID.fromString("48f685ee-e174-4055-b644-68b93cd5116f"), "M2", new BigDecimal("9.0"), Brand.MERCEDES, true),
        };
    }

    @Override
    public Car[] findAll() {
        return cars;
    }
}
