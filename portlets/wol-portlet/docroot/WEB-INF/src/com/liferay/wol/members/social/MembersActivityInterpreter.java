/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.wol.members.social;

import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;

/**
 * <a href="MembersActivityInterpreter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MembersActivityInterpreter extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	protected SocialActivityFeedEntry doInterpret(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		String creatorUserName = getUserName(
			activity.getUserId(), themeDisplay);

		User creatorUser = UserLocalServiceUtil.getUserById(
			activity.getUserId());

		int activityType = activity.getType();

		// Link

		StringMaker sm = null;

		String link = StringPool.BLANK;

		if (activityType == MembersActivityKeys.ADD_MEMBER) {
			sm = new StringMaker();

			sm.append(themeDisplay.getURLPortal());
			sm.append(themeDisplay.getPathFriendlyURLPublic());
			sm.append(StringPool.SLASH);
			sm.append(creatorUser.getScreenName());
			sm.append("/profile");

			link = sm.toString();
		}

		// Title

		Organization organization =
			OrganizationLocalServiceUtil.getOrganization(activity.getClassPK());

		Group group = organization.getGroup();

		String title = StringPool.BLANK;

		if (activityType == MembersActivityKeys.ADD_MEMBER) {
			sm = new StringMaker();

			sm = new StringMaker();

			sm.append("<a href=\"");
			sm.append(link);
			sm.append("\">");
			sm.append(creatorUserName);
			sm.append("</a>");

			String creatorUserNameURL = sm.toString();

			sm = new StringMaker();

			sm.append("<a href=\"");
			sm.append(themeDisplay.getURLPortal());
			sm.append(themeDisplay.getPathFriendlyURLPublic());
			sm.append(group.getFriendlyURL());
			sm.append("/profile\">");
			sm.append(organization.getName());
			sm.append("</a>");

			String organizationNameURL = sm.toString();

			title = themeDisplay.translate(
				"activity-wol-summary-join-organization",
				new Object[] {creatorUserNameURL, organizationNameURL});
		}

		// Body

		String body = StringPool.BLANK;

		return new SocialActivityFeedEntry(title, body, link);
	}

	private static final String[] _CLASS_NAMES = new String[] {
		Organization.class.getName()
	};

}