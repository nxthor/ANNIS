/*
 * Copyright 2009-2011 Collaborative Research Centre SFB 632 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package annis.sqlgen.model;

import annis.model.AnnisNode;

// FIXME: same as Dominance, abstract range information and refactor generation code in ClauseAnalysis
public class PointingRelation extends RankTableJoin {

	public PointingRelation(AnnisNode target, String name) {
		this(target, name, 0, 0);
	}
	
	public PointingRelation(AnnisNode target, String name, int distance) {
		this(target, name, distance, distance);
	}
	
	public PointingRelation(AnnisNode target, String name, int minDistance, int maxDistance) {
		super(target, name, minDistance, maxDistance);
	}
	
	@Override
	public String toString() {
		return "points to node " + target.getId() + " (" + name + ", " + minDistance + ", " + maxDistance + ")";
	}

}
