package com.wtb.javatool.httpApi;

import com.fy.javanode.declarative.annotation.*;

@HttpApi(urlFieldPath = "com.fy.tre.system.SystemConstants OPENINTERFACE_URL", version = HttpApi.VERSION_2)
@Param(name = "format", value="json")
@Param(name = "aid", value = "21419389378723787447")
@AccessToken
public interface CoreApi {
	@Get("/core/v4/band/|bandID|?")
	@Param(name = "field", value = "[\"bandID\",\"organizationID\"]")
	@Param(name = "gid", value = "|bandID|")
	String getBandByBandID(@Mount("bandID") Long bandID);

	@Post("/core/v4/user/4749755/band/1/bandRoles?")
	@Param(name = "gid", value = "1")
	@FormField(name = "bandRoleIDs ", value="[\"404750048\"]")
	String addRole();
}
