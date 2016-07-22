/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.tms;

import java.util.Optional;

import org.openwms.common.CommonGateway;
import org.openwms.common.TransportUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A ChangeTUFunction is responsible to change a {@link TransportOrder}s assigned {@code TransportUnit}.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.0
 */
@Component
class ChangeTUFunction implements UpdateFunction {

    @Autowired
    private CommonGateway gateway;

    @Override
    public TransportOrder update(TransportOrder saved, TransportOrder toUpdate) {
        if (saved.getTransportUnitBK().equalsIgnoreCase(toUpdate.getTransportUnitBK())) {
            // change TU.targetLocation

            TransportUnit savedTU = new TransportUnit();
            savedTU.setBk(toUpdate.getTransportUnitBK());
            savedTU.setTarget(toUpdate.getTargetLocationGroup());
            // synchronizing ...
            gateway.updateTransportUnit(savedTU);

            Optional<TransportUnit> tuOpt = gateway.getTransportUnit(saved.getTransportUnitBK());
            if (tuOpt.isPresent()){
                TransportUnit tu = tuOpt.get();
                tu.clearTarget();
                gateway.updateTransportUnit(tu);
            }

        }
        return saved;
    }
}