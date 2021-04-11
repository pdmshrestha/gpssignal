import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:location/location.dart';

class LocationService extends StateNotifier<AsyncValue<PermissionStatus>> {
  final Location _location = Location();

  LocationService([
    AsyncValue<PermissionStatus>? state,
  ]) : super(state ?? const AsyncValue.loading()) {
    checkPermission();
  }

  Future<void> checkPermission() async {
    state = const AsyncValue.loading();
    final PermissionStatus perm = await _location.hasPermission();
    state = AsyncValue.data(perm);
  }

  Future<void> requestPermission() async {
    state = const AsyncValue.loading();
    final perm = await _location.requestPermission();
    print("Permission $perm");
    state = AsyncValue.data(perm);
  }
}

final locationProvider =
    StateNotifierProvider<LocationService, AsyncValue<PermissionStatus>>(
        (ref) => LocationService());
