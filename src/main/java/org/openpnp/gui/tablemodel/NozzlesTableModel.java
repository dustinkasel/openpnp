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

package org.openpnp.gui.tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.openpnp.ConfigurationListener;
import org.openpnp.gui.support.HeadCellValue;
import org.openpnp.model.Configuration;
import org.openpnp.spi.Head;
import org.openpnp.spi.Nozzle;

public class NozzlesTableModel extends AbstractTableModel {
	final private Configuration configuration;
	
	private String[] columnNames = new String[] { "Name", "Type", "Head" };
	private List<Nozzle> nozzles;

	public NozzlesTableModel(Configuration configuration) {
		this.configuration = configuration;
        Configuration.get().addListener(new ConfigurationListener.Adapter() {
            public void configurationComplete(Configuration configuration) throws Exception {
                refresh();
            }
        });
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return (nozzles == null) ? 0 : nozzles.size();
	}
	
	public Nozzle getNozzle(int index) {
		return nozzles.get(index);
	}
	
	public void refresh() {
	    nozzles = new ArrayList<Nozzle>();
		for (Head head : Configuration.get().getMachine().getHeads()) {
	        nozzles.addAll(head.getNozzles());
		}
		fireTableDataChanged();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	    return false;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 2) {
			return HeadCellValue.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}
	
	public Object getValueAt(int row, int col) {
	    Nozzle nozzle = nozzles.get(row);
		switch (col) {
        case 0:
            return nozzle.getName();
        case 1:
            return nozzle.getClass().getSimpleName();
		case 2:
            return new HeadCellValue(nozzle.getHead());
			
		default:
			return null;
		}
	}
}