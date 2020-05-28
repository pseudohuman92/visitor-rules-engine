package com.visitor.helpers.containers;

import java.util.function.Supplier;

public class ActivatedAbility {

    public final Supplier<Boolean> canActivate;
    public final Runnable activate;


    public ActivatedAbility(Supplier<Boolean> canActivate, Runnable activate) {
        this.canActivate = canActivate;
        this.activate = activate;
    }
}
