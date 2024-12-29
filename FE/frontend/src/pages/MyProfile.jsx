import React, { useContext, useState } from 'react';
import { AppContext } from '../context/AppContext';
import axios from 'axios';
import { toast } from 'react-toastify';
import { assets } from '../assets/assets';

const MyProfile = () => {
    const [isEdit, setIsEdit] = useState(false);
    const [image, setImage] = useState(null);  // Khởi tạo là null thay vì false

    const { userId, token, backendUrl, userData, setUserData, loadUserProfileData } = useContext(AppContext);

    // Function to update user profile data using API
    const updateUserProfileData = async () => {
        try {
            const formData = new FormData();
            formData.append('userId', userId);
            formData.append('fullName', userData.fullName);
            formData.append('phone', userData.phone);
            formData.append('address', userData.address);
            formData.append('sex', userData.sex);
            formData.append('dob', userData.dob);

            // Thêm ảnh nếu có
            if (image) formData.append('image', image);

            const { data } = await axios.post(backendUrl + '/api/user/update-profile', formData, {
                headers: { token }
            });

            if (data.success) {
                toast.success(data.message);
                await loadUserProfileData();
                setIsEdit(false);
                setImage(null); // Đặt lại image khi lưu thành công
            } else {
                toast.error(data.message);
            }
        } catch (error) {
            console.log(error);
            toast.error(error.message);
        }
    };

    return userData ? (
        <div className='max-w-lg flex flex-col gap-2 text-sm pt-5'>
            {/* Hình ảnh người dùng */}
            {isEdit ? (
                <label htmlFor='image'>
                    <div className='inline-block relative cursor-pointer'>
                        <img className='w-36 rounded opacity-75' src={image ? URL.createObjectURL(image) : userData.image} alt="" />
                        <img className='w-10 absolute bottom-12 right-12' src={image ? '' : assets.upload_icon} alt="" />
                    </div>
                    <input onChange={(e) => setImage(e.target.files[0])} type="file" id="image" hidden />
                </label>
            ) : (
                <img className='w-36 rounded' src={userData.image} alt="" />
            )}

            {/* Tên người dùng */}
            {isEdit ? (
                <input
                    className='bg-gray-50 text-3xl font-medium max-w-60'
                    type="text"
                    onChange={(e) => setUserData(prev => ({ ...prev, fullName: e.target.value }))}
                    value={userData.fullName}
                />
            ) : (
                <p className='font-medium text-3xl text-[#262626] mt-4'>{userData.fullName}</p>
            )}

            <hr className='bg-[#ADADAD] h-[1px] border-none' />

            {/* Thông tin liên hệ */}
            <div>
                <p className='text-gray-600 underline mt-3'>CONTACT INFORMATION</p>
                <div className='grid grid-cols-[1fr_3fr] gap-y-2.5 mt-3 text-[#363636]'>
                    <p className='font-medium'>Email address:</p>
                    <p className='text-blue-500'>{userData.email}</p>
                    <p className='font-medium'>Phone:</p>
                    {isEdit ? (
                        <input
                            className='bg-gray-50 max-w-52'
                            type="text"
                            onChange={(e) => setUserData(prev => ({ ...prev, phone: e.target.value }))}
                            value={userData.phone}
                        />
                    ) : (
                        <p className='text-blue-500'>{userData.phone}</p>
                    )}
                    <p className='font-medium'>Address:</p>
                    {isEdit ? (
                        <input
                            className='bg-gray-50'
                            type="text"
                            onChange={(e) => setUserData(prev => ({ ...prev, address: e.target.value }))}
                            value={userData.address}
                        />
                    ) : (
                        <p className='text-gray-500'>{userData.address}</p>
                    )}
                </div>
            </div>

            {/* Thông tin cơ bản */}
            <div>
                <p className='text-[#797979] underline mt-3'>BASIC INFORMATION</p>
                <div className='grid grid-cols-[1fr_3fr] gap-y-2.5 mt-3 text-gray-600'>
                    <p className='font-medium'>Gender:</p>
                    {isEdit ? (
                        <select
                            className='max-w-20 bg-gray-50'
                            onChange={(e) => setUserData(prev => ({ ...prev, sex: e.target.value }))}
                            value={userData.sex}
                        >
                            <option value="Not Selected">Not Selected</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Others">Others</option>
                        </select>
                    ) : (
                        <p className='text-gray-500'>{userData.sex}</p>
                    )}

                    <p className='font-medium'>Birthday:</p>
                    {isEdit ? (
                        <input
                            className='max-w-28 bg-gray-50'
                            type='date'
                            onChange={(e) => setUserData(prev => ({ ...prev, dob: e.target.value }))}
                            value={userData.dob}
                        />
                    ) : (
                        <p className='text-gray-500'>{userData.dob}</p>
                    )}
                </div>
            </div>

            {/* Button Edit or Save */}
            <div className='mt-10'>
                {isEdit ? (
                    <button onClick={updateUserProfileData} className='border border-primary px-8 py-2 rounded-full hover:bg-primary hover:text-white transition-all'>
                        Save information
                    </button>
                ) : (
                    <button onClick={() => setIsEdit(true)} className='border border-primary px-8 py-2 rounded-full hover:bg-primary hover:text-white transition-all'>
                        Edit
                    </button>
                )}
            </div>
        </div>
    ) : null;
};

export default MyProfile;
