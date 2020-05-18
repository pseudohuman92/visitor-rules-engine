package com.visitor.card.types.helpers;

import java.util.function.Supplier;

public class ActivatedAbility {

    public final Supplier<Boolean> canActivate;
    public final Runnable activate;


    public ActivatedAbility(Supplier<Boolean> canActivate, Runnable activate) {
        this.canActivate = canActivate;
        this.activate = activate;
    }
}
