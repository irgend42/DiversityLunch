import { Dispatch } from '@reduxjs/toolkit';
import { CATEGORY_ENDPOINT } from '../data/category/fetch-category';
import {
    accountAdminData,
    accountAzureAdminData,
    accountList,
    accountStandardData,
    categoryData,
    countryData,
    dietData,
    educationData,
    genderData,
    hobbyData,
    languageData,
    profileData,
    profileList,
    projectData,
    religionData,
    userVoucherList,
    workExperienceData,
} from './data';
import { COUNTRY_ENDPOINT } from '../data/country/fetch-country';
import { DIET_ENDPOINT } from '../data/diet/fetch-diet';
import { EDUCATION_ENDPOINT } from '../data/education/fetch-education';
import { GENDER_ENDPOINT } from '../data/gender/fetch-gender';
import { LANGUAGE_ENDPOINT } from '../data/language/language-fetch';
import { PROJECT_ENDPOINT } from '../data/project/project-fetch';
import { RELIGION_ENDPOINT } from '../data/religion/religion-fetch';
import { WORK_EXPERIENCE_ENDPOINT } from '../data/work-experience/work-experience-fetch';
import { HOBBY_ENDPOINT } from '../data/hobby/fetch-hobby';
import { PROFILE_ENDPOINT } from '../data/profile/profile.actions';
import { Role } from '../model/Role';
import { Account } from '../types/Account';
import { accountsAction } from '../data/accounts/accounts-reducer';
import { profilesAction } from '../data/profiles/profiles-reducer';
import { Profile } from '../model/Profile';
import { handleFetchResponse } from '../data/handleFetchResponse';
import { FetchCallbacks } from '../data/generic/FetchCallbacks';

export const mockedFetchGetProfile = async (url: string) => {
    if (url.includes(CATEGORY_ENDPOINT)) {
        return new Response(JSON.stringify(categoryData));
    }
    if (url.includes(COUNTRY_ENDPOINT)) {
        return new Response(JSON.stringify(countryData));
    }
    if (url.includes(DIET_ENDPOINT)) {
        return new Response(JSON.stringify(dietData));
    }
    if (url.includes(EDUCATION_ENDPOINT)) {
        return new Response(JSON.stringify(educationData));
    }
    if (url.includes(GENDER_ENDPOINT)) {
        return new Response(JSON.stringify(genderData));
    }
    if (url.includes(LANGUAGE_ENDPOINT)) {
        return new Response(JSON.stringify(languageData));
    }
    if (url.includes(PROJECT_ENDPOINT)) {
        return new Response(JSON.stringify(projectData));
    }
    if (url.includes(RELIGION_ENDPOINT)) {
        return new Response(JSON.stringify(religionData));
    }
    if (url.includes(WORK_EXPERIENCE_ENDPOINT)) {
        return new Response(JSON.stringify(workExperienceData));
    }
    if (url.includes(HOBBY_ENDPOINT)) {
        return new Response(JSON.stringify(hobbyData));
    }
    if (url.includes(PROFILE_ENDPOINT)) {
        return new Response(JSON.stringify(profileData[0]));
    }
    return new Response('');
};

export const mockedFetchGetAccount = (role: Role) => async (url: string) => {
    if (url.includes('/api/account')) {
        return mockedFetchGetAccountByRole(role);
    }
    return new Response('');
};
export const mockedFetchGetUsableAccount = () => async () => new Response(JSON.stringify(accountList[0]));
export const mockedFetchGetAccountByRole = async (role: Role) => {
    if (role === Role.STANDARD) {
        return new Response(JSON.stringify(accountStandardData));
    }
    if (role === Role.ADMIN) {
        return new Response(JSON.stringify(accountAdminData));
    }
    if (role === Role.AZURE_ADMIN) {
        return new Response(JSON.stringify(accountAzureAdminData));
    }
    return new Response('');
};

export const mockedFetchGetAccounts = (callbacks: FetchCallbacks) => async (dispatch: Dispatch) => {
    const onGoingFetch = Promise.resolve(new Response(JSON.stringify(accountList), { status: 200, statusText: 'ok' }));
    const onSuccess = async (response: Response) => {
        try {
            const result: Account[] = await response.json();
            dispatch(accountsAction.update(result));
            dispatch(accountsAction.initFetch(undefined));
        } catch {
            callbacks.onNetworkError(new Error("Couldn't parse json"));
        }
    };
    handleFetchResponse(onGoingFetch, onSuccess, callbacks);
};

export const mockedFetchGetProfiles = (callbacks: FetchCallbacks) => async (dispatch: Dispatch) => {
    const onGoingFetch = Promise.resolve(new Response(JSON.stringify(profileList), { status: 200, statusText: 'ok' }));
    const onSuccess = async (response: Response) => {
        try {
            const result : Profile[] = await response.json();
            dispatch(profilesAction.update(result));
            dispatch(profilesAction.initFetch(undefined));
        } catch {
            callbacks.onNetworkError(new Error("Couldn't parse json"));
        }
    };
    handleFetchResponse(onGoingFetch, onSuccess, callbacks);
};

export const mockedRevokeAdminRole = (accountId: number, callbacks: FetchCallbacks) => async (dispatch: Dispatch) => {
    const tempAccount = accountList.find((account) => account.id === accountId);
    if (tempAccount !== undefined) {
        tempAccount.role = Role.STANDARD;
    }
    const onGoingFetch = Promise.resolve(new Response(JSON.stringify(tempAccount), { status: 200, statusText: 'ok' }));
    const onSuccess = async (response: Response) => {
        try {
            const result: Account = await response.json();
            dispatch(accountsAction.update([result]));
        } catch {
            callbacks.onNetworkError(new Error("Couldn't parse json"));
        }
    };
    handleFetchResponse(onGoingFetch, onSuccess, callbacks);
};

export const mockedAssignAdminRole = (accountId: number, callbacks: FetchCallbacks) => async (dispatch: Dispatch) => {
    const tempAccount = accountList.find((account) => account.id === accountId);
    if (tempAccount !== undefined) {
        tempAccount.role = Role.ADMIN;
    }
    const onGoingFetch = Promise.resolve(new Response(JSON.stringify(tempAccount), { status: 200, statusText: 'ok' }));
    const onSuccess = async (response: Response) => {
        try {
            const result : Account = await response.json();
            dispatch(accountsAction.update([result]));
        } catch {
            callbacks.onNetworkError(new Error("Couldn't parse json"));
        }
    };
    handleFetchResponse(onGoingFetch, onSuccess, callbacks);
};

export const mockedFetchGetUserVouchers = () => {
    const onGoingFetch = Promise.resolve(new Response(JSON.stringify(userVoucherList), {
        status: 200,
        statusText: 'ok',
    }));
    return onGoingFetch;
};
