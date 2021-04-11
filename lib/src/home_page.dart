import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:gps_signal/src/location_page.dart';
import 'package:gps_signal/src/location_service.dart';
import 'package:gps_signal/src/permission_page.dart';
import 'package:location/location.dart';

class HomePage extends ConsumerWidget {
  @override
  Widget build(BuildContext context, ScopedReader watch) {
    final _ctrl = watch(locationProvider);
    return _ctrl.when(
      error: (e, s) => CenterPage(child: Text(e.toString())),
      loading: () => const CenterPage(child: Text("Loading...")),
      data: (data) => data == PermissionStatus.granted ||
              data == PermissionStatus.grantedLimited
          ? LocationPage()
          : PermissionPage(),
    );
  }
}

class CenterPage extends StatelessWidget {
  final Widget child;
  const CenterPage({Key? key, required this.child}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(child: child),
    );
  }
}
