/*
 	Copyright (C) 2011 Jason von Nieda <jason@vonnieda.org>
 	
 	This file is part of OpenPnP.
 	
	OpenPnP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenPnP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenPnP.  If not, see <http://www.gnu.org/licenses/>.
 	
 	For more information about OpenPnP visit http://openpnp.org
 */

package org.openpnp.machine.reference;

import java.util.ArrayList;
import java.util.List;

import org.openpnp.ConfigurationListener;
import org.openpnp.gui.support.Wizard;
import org.openpnp.machine.reference.camera.LtiCivilCamera;
import org.openpnp.machine.reference.camera.OpenCvCamera;
import org.openpnp.machine.reference.camera.TableScannerCamera;
import org.openpnp.machine.reference.camera.VfwCamera;
import org.openpnp.machine.reference.feeder.ReferenceTapeFeeder;
import org.openpnp.machine.reference.feeder.ReferenceTrayFeeder;
import org.openpnp.machine.reference.feeder.ReferenceTubeFeeder;
import org.openpnp.model.Configuration;
import org.openpnp.spi.Camera;
import org.openpnp.spi.Feeder;
import org.openpnp.spi.base.AbstractMachine;
import org.simpleframework.xml.Element;

public class ReferenceMachine extends AbstractMachine {
	@Element
	private ReferenceDriver driver;
	
	private boolean enabled;
	
	public ReferenceMachine() {
	    /**
	     * Note: The pattern below is used so that subclasses do not interfere
	     * with this class getting the Configuration information it needs. If
	     * the class instead just implemented ConfigurationListener then a
	     * subclass might override this.
	     */
	    Configuration.get().addListener(new ConfigurationListener() {
            @Override
            public void configurationLoaded(Configuration configuration)
                    throws Exception {
                configuration.resolve(driver);
            }
        });
	}
	
	ReferenceDriver getDriver() {
		return driver;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enabled) throws Exception {
		if (enabled) {
			try {
				driver.setEnabled(true);
				this.enabled = true;
			}
			catch (Exception e) {
				fireMachineEnableFailed(e.getMessage());
				throw e;
			}
			fireMachineEnabled();
		}
		else {
			try {
				driver.setEnabled(false);
				this.enabled = false;
			}
			catch (Exception e) {
				fireMachineDisableFailed(e.getMessage());
				throw e;
			}
			fireMachineDisabled("User requested stop.");
		}
	}

	@Override
	public Wizard getConfigurationWizard() {
		return null;
	}

	@Override
	public List<Class<? extends Feeder>> getCompatibleFeederClasses() {
		List<Class<? extends Feeder>> l = new ArrayList<Class<? extends Feeder>>();
		l.add(ReferenceTrayFeeder.class);
		l.add(ReferenceTapeFeeder.class);
		l.add(ReferenceTubeFeeder.class);
		return l;
	}

	@Override
	public List<Class<? extends Camera>>  getCompatibleCameraClasses() {
		List<Class<? extends Camera>> l = new ArrayList<Class<? extends Camera>>();
		l.add(LtiCivilCamera.class);
		l.add(VfwCamera.class);
		l.add(TableScannerCamera.class);
		l.add(OpenCvCamera.class);
		return l;
	}
}
