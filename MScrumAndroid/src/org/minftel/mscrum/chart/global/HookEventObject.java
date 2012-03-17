/*
Copyright 2010 Kxu
Copyright 2010 TheChatrouletteGirls.Com.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.minftel.mscrum.chart.global;

import org.minftel.mscrum.chart.flot.FlotDraw;

/**
 * 
 * @author Administrator
 *
 */
public class HookEventObject {
	public FlotDraw fd;
	/**
	 * hookParam in each hook is used to 
	 * @see FlotEvent
	 */
	public Object[] hookParam;
	
	public HookEventObject(FlotDraw fd, Object[] hookParam) {
		this.fd = fd;
		this.hookParam = hookParam;
	}
}
