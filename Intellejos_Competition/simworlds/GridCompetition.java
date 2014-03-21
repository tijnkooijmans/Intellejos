/*

Copyright 2004 Tijn Kooijmans <intellejos@kooijmans.nu>

This file is part of Intellejos. Intellejos is a modification of Intellego,
developed by Graham Ritchie.

Intellejos is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Intellejos is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Intellego; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/

import simworldobjects.*;

/**
* Models a 5 x 5 grid layout
*
* @author Tijn Kooijmans
*/

public class GridCompetition extends BasicSimWorld
{

	public GridCompetition()
	{
		// initialise the world size
		super(900,900,900);

		SimCrossing crossing1=new SimCrossing(100.0,0.0,100.0,0.0,80.0,80.0);
		addObject(crossing1);

		SimCrossing crossing2=new SimCrossing(100.0,0.0,275.0,0.0,80.0,80.0);
		addObject(crossing2);

		SimCrossing crossing3=new SimCrossing(100.0,0.0,450.0,0.0,80.0,80.0);
		addObject(crossing3);

		SimCrossing crossing4=new SimCrossing(100.0,0.0,625.0,0.0,80.0,80.0);
		addObject(crossing4);

		SimCrossing crossing5=new SimCrossing(100.0,0.0,800.0,0.0,80.0,80.0);
		addObject(crossing5);

		SimCrossing crossing6=new SimCrossing(275.0,0.0,100.0,0.0,80.0,80.0);
		addObject(crossing6);

		SimCrossing crossing7=new SimCrossing(275.0,0.0,275.0,0.0,80.0,80.0);
		addObject(crossing7);

		SimCrossing crossing8=new SimCrossing(275.0,0.0,450.0,0.0,80.0,80.0);
		addObject(crossing8);

		SimCrossing crossing9=new SimCrossing(275.0,0.0,625.0,0.0,80.0,80.0);
		addObject(crossing9);

		SimCrossing crossing10=new SimCrossing(275.0,0.0,800.0,0.0,80.0,80.0);
		addObject(crossing10);

		SimCrossing crossing11=new SimCrossing(450.0,0.0,100.0,0.0,80.0,80.0);
		addObject(crossing11);

		SimCrossing crossing12=new SimCrossing(450.0,0.0,275.0,0.0,80.0,80.0);
		addObject(crossing12);

		SimCrossing crossing13=new SimCrossing(450.0,0.0,450.0,0.0,80.0,80.0);
		addObject(crossing13);

		SimCrossing crossing14=new SimCrossing(450.0,0.0,625.0,0.0,80.0,80.0);
		addObject(crossing14);

		SimCrossing crossing15=new SimCrossing(450.0,0.0,800.0,0.0,80.0,80.0);
		addObject(crossing15);

		SimCrossing crossing16=new SimCrossing(625.0,0.0,100.0,0.0,80.0,80.0);
		addObject(crossing16);

		SimCrossing crossing17=new SimCrossing(625.0,0.0,275.0,0.0,80.0,80.0);
		addObject(crossing17);

		SimCrossing crossing18=new SimCrossing(625.0,0.0,450.0,0.0,80.0,80.0);
		addObject(crossing18);

		SimCrossing crossing19=new SimCrossing(625.0,0.0,625.0,0.0,80.0,80.0);
		addObject(crossing19);

		SimCrossing crossing20=new SimCrossing(625.0,0.0,800.0,0.0,80.0,80.0);
		addObject(crossing20);

		SimCrossing crossing21=new SimCrossing(800.0,0.0,100.0,0.0,80.0,80.0);
		addObject(crossing21);

		SimCrossing crossing22=new SimCrossing(800.0,0.0,275.0,0.0,80.0,80.0);
		addObject(crossing22);

		SimCrossing crossing23=new SimCrossing(800.0,0.0,450.0,0.0,80.0,80.0);
		addObject(crossing23);

		SimCrossing crossing24=new SimCrossing(800.0,0.0,625.0,0.0,80.0,80.0);
		addObject(crossing24);

		SimCrossing crossing25=new SimCrossing(800.0,0.0,800.0,0.0,80.0,80.0);
		addObject(crossing25);



		SimPath path1=new SimPath(450.0,0.0,100.0,0.0,900.0,20.0);
		addObject(path1);

		SimPath path2=new SimPath(450.0,0.0,275.0,0.0,900.0,20.0);
		addObject(path2);

		SimPath path9=new SimPath(450.0,0.0,450.0,0.0,900.0,20.0);
		addObject(path9);

		SimPath path10=new SimPath(450.0,0.0,625.0,0.0,900.0,20.0);
		addObject(path10);

		SimPath path11=new SimPath(450.0,0.0,800.0,0.0,900.0,20.0);
		addObject(path11);


		SimPath path4=new SimPath(100.0,0.0,450.0,90.0,900.0,20.0);
		addObject(path4);

		SimPath path5=new SimPath(275.0,0.0,450.0,90.0,900.0,20.0);
		addObject(path5);

		SimPath path6=new SimPath(450.0,0.0,450.0,90.0,900.0,20.0);
		addObject(path6);

		SimPath path7=new SimPath(625.0,0.0,450.0,90.0,900.0,20.0);
		addObject(path7);

		SimPath path8=new SimPath(800.0,0.0,450.0,90.0,900.0,20.0);
		addObject(path8);
	}
}
